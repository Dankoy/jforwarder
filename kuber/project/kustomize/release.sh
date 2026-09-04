#!/bin/bash

## Kustomize release of the jforwarder manifests, without the sed templating
## of ../../release.sh:
##
##   ./release.sh version              # writes the build.gradle version into
##                                     # overlays/production, commit the diff
##   ./release.sh install -u <user>    # kubectl apply -k with that version
##
## Every environment is an overlay of overlays/, chosen with -o and defaulting
## to production. The version lives in git (overlays/<env>/kustomization.yaml),
## the registry and the docker hub user do not, they are flags of "install".

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
OVERLAYS_DIR="${SCRIPT_DIR}/overlays"
DEFAULT_ENVIRONMENT="production"
RELEASE_DIR="${OVERLAYS_DIR}/release"
RELEASE_FILE="${RELEASE_DIR}/kustomization.yaml"
BUILD_GRADLE="${SCRIPT_DIR}/../../../build.gradle"

# Image names of base/deployments, docker hub repository names as well.
IMAGES=(
  coub_smart_searcher
  kafka_message_consumer
  kafka_message_producer
  spring_eureka_registry
  spring_gateway
  subscriptions_holder
  subscriptions_scheduler
  t_coubs_initiator
  coub_forwarder_telegram_bot
  telegram_chat_service
)

### the overlay of an environment ############################################

overlay_dir() {
  local environment=$1
  local dir="${OVERLAYS_DIR}/${environment}"

  if [ ! -f "${dir}/kustomization.yaml" ]; then
    printf "Unknown environment %s, available: %s\n" "${environment}" \
      "$(ls "${OVERLAYS_DIR}" | grep -v '^release$' | tr '\n' ' ')" >&2
    exit 1
  fi

  echo "${dir}"
}

Help() {
  echo "Releases the jforwarder manifests with kustomize"
  echo
  echo "Syntax: release.sh <command> [options]"
  echo
  echo "commands:"
  echo "  version   Write the image tag into overlays/production, the file"
  echo "            that keeps the deployed version in git. Nothing is sent"
  echo "            to the cluster, the diff is meant to be committed."
  echo "  install   Apply the manifests with kubectl apply -k."
  echo
  echo "version options:"
  echo "  -t  Image tag. Default: the version of build.gradle."
  echo "  -o  Environment: production, dev, test. Default: production."
  echo "  -h  Print this help."
  echo
  echo "install options:"
  echo "  -o  Environment: production, dev, test. Default: production."
  echo "  -u  Docker registry user. Without it the images are taken as they"
  echo "      are, which is what locally built k3d images need."
  echo "  -H  Registry domain. Default: docker.io. Needs -u."
  echo "  -d  Dry run, renders the manifests without touching the cluster."
  echo "  -h  Print this help."
  echo
  echo "Example:"
  echo "  ./release.sh version -t 1.9.6-SNAPSHOT"
  echo "  ./release.sh install -u <docker hub user>"
  echo "  ./release.sh version -o dev && ./release.sh install -o dev"
}

### the version of build.gradle, the same one publish.yml tags images with ####

gradle_version() {
  local version
  version=$(sed -n 's/.*set("PROJECT_VERSION", "\([^"]*\)").*/\1/p' \
    "${BUILD_GRADLE}")

  if [ -z "${version}" ]; then
    echo "PROJECT_VERSION not found in ${BUILD_GRADLE}, pass the tag with -t" >&2
    exit 1
  fi

  # allprojects { version = "${PROJECT_VERSION}-SNAPSHOT" }
  echo "${version}-SNAPSHOT"
}

### version: set the tag of every image in the tracked overlay ###############

cmd_version() {
  local tag="" environment="${DEFAULT_ENVIRONMENT}"

  OPTIND=1
  while getopts ":t:o:h" opt; do
    case ${opt} in
      h) Help; exit 0 ;;
      t) tag=${OPTARG} ;;
      o) environment=${OPTARG} ;;
      :) printf "Option -%s requires an argument.\n" "${OPTARG}"; exit 1 ;;
      ?) printf "Invalid option: -%s.\n" "${OPTARG}"; exit 1 ;;
    esac
  done

  local overlay_file
  overlay_file="$(overlay_dir "${environment}")/kustomization.yaml"

  if [ -z "${tag}" ]; then
    tag=$(gradle_version)
    printf "Taking the version of build.gradle: %s\n" "${tag}"
  fi

  # Same edit "kustomize edit set image <image>:<tag>" would do, without
  # asking for the kustomize binary: only the newTag of the known images is
  # rewritten, everything else in the file is left alone.
  local tmp="${overlay_file}.tmp"
  awk -v tag="${tag}" -v images="${IMAGES[*]}" '
    BEGIN { split(images, list, " "); for (i in list) known[list[i]] = 1 }
    /^  - name: / { current = $3; print; next }
    /^    newTag: / && current in known { printf "    newTag: \"%s\"\n", tag; next }
    { print }
  ' "${overlay_file}" > "${tmp}"
  mv "${tmp}" "${overlay_file}"

  printf "\nWrote %s\n tag: %s\n\nCommit it, it is the deployed version.\n\n" \
    "${overlay_file}" "${tag}"
}

### install: apply the overlay ###############################################

cmd_install() {
  local user="" registry="docker.io" dry_run="" environment="${DEFAULT_ENVIRONMENT}"

  OPTIND=1
  while getopts ":u:H:o:dh" opt; do
    case ${opt} in
      h) Help; exit 0 ;;
      u) user=${OPTARG} ;;
      H) registry=${OPTARG} ;;
      o) environment=${OPTARG} ;;
      d) dry_run="yes" ;;
      :) printf "Option -%s requires an argument.\n" "${OPTARG}"; exit 1 ;;
      ?) printf "Invalid option: -%s.\n" "${OPTARG}"; exit 1 ;;
    esac
  done

  local overlay
  overlay="$(overlay_dir "${environment}")"

  # docker.io/<user>/image, the registry part of the old release.sh. It is
  # not committed, so it is layered on top of the tracked overlay instead.
  if [ -n "${user}" ]; then
    local prefix=""
    [ -n "${registry}" ] && prefix="${registry}/"
    prefix="${prefix}${user}/"

    mkdir -p "${RELEASE_DIR}"
    cat > "${RELEASE_FILE}" <<HEADER
---
# Generated by release.sh install -o ${environment} -u ${user} -H ${registry}
# The tag comes from ../${environment}, this only prepends the registry.
# Do not edit and do not commit it.
apiVersion: kustomize.config.k8s.io/v1beta1
kind: Kustomization

resources:
  - ../${environment}

images:
HEADER
    for image in "${IMAGES[@]}"; do
      cat >> "${RELEASE_FILE}" <<ENTRY
  - name: ${image}
    newName: ${prefix}${image}
ENTRY
    done

    overlay="${RELEASE_DIR}"
  fi

  if [ -n "${dry_run}" ]; then
    kubectl kustomize "${overlay}"
    exit 0
  fi

  printf "\nApplying %s\n\n" "${overlay}"

  kubectl apply -k "${overlay}"
}

### dispatch #################################################################

COMMAND="${1:-}"
if [ $# -gt 0 ]; then
  shift
fi

case "${COMMAND}" in
  version) cmd_version "$@" ;;
  install) cmd_install "$@" ;;
  -h|--help|help) Help; exit 0 ;;
  "") echo "Missing command" >&2; Help; exit 1 ;;
  *) printf "Unknown command: %s\n\n" "${COMMAND}" >&2; Help; exit 1 ;;
esac
