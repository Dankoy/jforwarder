#!/bin/bash

## Kustomize release of the jforwarder manifests, without the sed templating
## of ../../release.sh:
##
##   ./release.sh version   # writes the build.gradle version into the overlay
##                          # of the environment, commit the diff
##   ./release.sh install   # kubectl apply -k with that version
##   ./release.sh render    # print the manifests, touch nothing
##
## Only the command is passed on the command line, every setting comes from
## ../../.env.deploy: which environment, the docker hub user, the registry
## host. The version does not live there, it lives in git, in
## overlays/<environment>/kustomization.yaml.

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
OVERLAYS_DIR="${SCRIPT_DIR}/overlays"
RELEASE_DIR="${OVERLAYS_DIR}/release"
RELEASE_FILE="${RELEASE_DIR}/kustomization.yaml"
KUBER_DIR="$(cd "${SCRIPT_DIR}/../.." && pwd)"
ENV_FILE="${KUBER_DIR}/.env.deploy"
BUILD_GRADLE="${KUBER_DIR}/../build.gradle"

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

Help() {
  echo "Releases the jforwarder manifests with kustomize"
  echo
  echo "Syntax: release.sh <command>"
  echo
  echo "commands:"
  echo "  version   Write the image tag into the overlay of ENVIRONMENT, the"
  echo "            file that keeps the deployed version in git. The tag is"
  echo "            the version of build.gradle. Nothing is sent to the"
  echo "            cluster, the diff is meant to be committed."
  echo "  install   Apply the manifests with kubectl apply -k."
  echo "  render    Print the manifests without touching the cluster."
  echo
  echo "Settings come from ${ENV_FILE}:"
  echo "  ENVIRONMENT       production, dev or test. Default: production."
  echo "  DOCKER_HUB_USER   Empty means the images are taken as they are,"
  echo "                    which is what locally built k3d images need."
  echo "  REGISTRY_HOST     Default: docker.io. Ignored without a user."
}

### settings ##################################################################

load_env() {
  if [ ! -f "${ENV_FILE}" ]; then
    printf "%s not found, copy it from .env.deploy.example first\n" \
      "${ENV_FILE}" >&2
    exit 1
  fi

  # shellcheck source=/dev/null
  . "${ENV_FILE}"

  DOCKER_HUB_USER="${DOCKER_HUB_USER:-}"
  REGISTRY_HOST="${REGISTRY_HOST:-docker.io}"
  ENVIRONMENT="${ENVIRONMENT:-production}"
}

# every overlays/<name>/kustomization.yaml but the generated one
environments() {
  local dir name
  for dir in "${OVERLAYS_DIR}"/*/kustomization.yaml; do
    name=$(basename "$(dirname "${dir}")")
    if [ "${name}" != "release" ]; then
      printf "%s " "${name}"
    fi
  done
}

overlay_dir() {
  local dir="${OVERLAYS_DIR}/${ENVIRONMENT}"

  if [ ! -f "${dir}/kustomization.yaml" ]; then
    printf "Unknown ENVIRONMENT %s, available: %s\n" "${ENVIRONMENT}" \
      "$(environments)" >&2
    exit 1
  fi

  echo "${dir}"
}

### the version of build.gradle, the same one publish.yml tags images with ####

gradle_version() {
  local version
  version=$(sed -n 's/.*set("PROJECT_VERSION", "\([^"]*\)").*/\1/p' \
    "${BUILD_GRADLE}")

  if [ -z "${version}" ]; then
    echo "PROJECT_VERSION not found in ${BUILD_GRADLE}" >&2
    exit 1
  fi

  # allprojects { version = "${PROJECT_VERSION}-SNAPSHOT" }
  echo "${version}-SNAPSHOT"
}

### version: set the tag of every image in the tracked overlay ################

cmd_version() {
  local overlay_file tag
  overlay_file="$(overlay_dir)/kustomization.yaml"
  tag=$(gradle_version)

  printf "Taking the version of build.gradle: %s\n" "${tag}"

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

### the overlay that is actually applied ######################################

## The registry and the docker hub user are not committed, so they are layered
## on top of the tracked overlay instead of living in it.

resolve_overlay() {
  local environment_dir
  environment_dir="$(overlay_dir)"

  if [ -z "${DOCKER_HUB_USER}" ]; then
    echo "${environment_dir}"
    return
  fi

  local prefix=""
  if [ -n "${REGISTRY_HOST}" ]; then
    prefix="${REGISTRY_HOST}/"
  fi
  prefix="${prefix}${DOCKER_HUB_USER}/"

  mkdir -p "${RELEASE_DIR}"
  cat > "${RELEASE_FILE}" <<HEADER
---
# Generated from .env.deploy by release.sh, the tag comes from ../${ENVIRONMENT}
# and this only prepends the registry. Do not edit and do not commit it.
apiVersion: kustomize.config.k8s.io/v1beta1
kind: Kustomization

resources:
  - ../${ENVIRONMENT}

images:
HEADER
  local image
  for image in "${IMAGES[@]}"; do
    cat >> "${RELEASE_FILE}" <<ENTRY
  - name: ${image}
    newName: ${prefix}${image}
ENTRY
  done

  echo "${RELEASE_DIR}"
}

### install / render ##########################################################

cmd_install() {
  local overlay
  overlay="$(resolve_overlay)"

  printf "\nApplying %s\n\n" "${overlay}"

  kubectl apply -k "${overlay}"
}

cmd_render() {
  local overlay
  overlay="$(resolve_overlay)"

  kubectl kustomize "${overlay}"
}

### dispatch ##################################################################

COMMAND="${1:-}"

case "${COMMAND}" in
  -h|--help|help) Help; exit 0 ;;
esac

if [ $# -gt 1 ]; then
  printf "release.sh takes a command and nothing else, settings live in %s\n" \
    "${ENV_FILE}" >&2
  exit 1
fi

load_env

case "${COMMAND}" in
  version) cmd_version ;;
  install) cmd_install ;;
  render) cmd_render ;;
  "") echo "Missing command" >&2; Help; exit 1 ;;
  *) printf "Unknown command: %s\n\n" "${COMMAND}" >&2; Help; exit 1 ;;
esac
