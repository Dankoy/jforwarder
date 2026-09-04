#!/bin/bash

## applies the whole project to the jforwarder namespace with kustomize,
## which is the default deploy since #333:
##
##   ./apply-all.sh -u <docker hub user>   # registry images, tag from git
##   ./apply-all.sh                        # images as they are, k3d builds
##   ./apply-all.sh -o dev                 # another environment
##
## The image tag comes from project/kustomize/overlays/<environment>, the file
## that keeps the deployed version in git. Bump it with
## "project/kustomize/release.sh version -o <environment>" and commit it.
##
## The old sed flow (release.sh + kubectl apply -f) is still one flag away:
##
##   ./release.sh -u <user> -t <tag> && ./apply-all.sh -p

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
KUSTOMIZE_DIR="${SCRIPT_DIR}/project/kustomize"
ENVIRONMENT="production"

Help() {
  echo "Applies the jforwarder project to the cluster"
  echo
  echo "Syntax: apply-all.sh [-o environment] [-u user] [-H registry] [-p] [-h]"
  echo "options:"
  echo "  -o  Environment: production, dev, test. Default: production."
  echo "      Its namespace comes from the overlay, jforwarder for"
  echo "      production, jforwarder-<environment> for the others."
  echo "  -u  Docker registry user. Without it the images are taken as they"
  echo "      are, which is what locally built k3d images need."
  echo "  -H  Registry domain. Default: docker.io. Needs -u."
  echo "  -p  Plain manifests instead of kustomize: applies"
  echo "      project/<folder> the way it was done before kustomize."
  echo "      Needs release.sh to have generated project/deployments first."
  echo "      Production only."
  echo "  -h  Print this help."
}

USER_ARG=""
REGISTRY_ARG=""
PLAIN=""

while getopts ":o:u:H:ph" opt; do
  case ${opt} in
    h) Help; exit 0 ;;
    o) ENVIRONMENT=${OPTARG} ;;
    u) USER_ARG=${OPTARG} ;;
    H) REGISTRY_ARG=${OPTARG} ;;
    p) PLAIN="yes" ;;
    :) printf "Option -%s requires an argument. \n" "${OPTARG}"; exit 1 ;;
    ?) printf "Invalid option: -%s. \n" "${OPTARG}"; exit 1 ;;
  esac
done

OVERLAY_DIR="${KUSTOMIZE_DIR}/overlays/${ENVIRONMENT}"

if [ ! -f "${OVERLAY_DIR}/kustomization.yaml" ]; then
  environments=""
  for overlay in "${KUSTOMIZE_DIR}"/overlays/*/kustomization.yaml; do
    name=$(basename "$(dirname "${overlay}")")
    if [ "${name}" != "release" ]; then
      environments="${environments}${name} "
    fi
  done

  printf "Unknown environment %s, available: %s \n" "${ENVIRONMENT}" \
    "${environments}"
  exit 1
fi

## the namespace of the environment, as the overlay declares it

NAMESPACE=$(sed -n 's/^namespace: *//p' "${OVERLAY_DIR}/kustomization.yaml")
NAMESPACE=${NAMESPACE:-jforwarder}

## dev and test bring their own namespace, production expects the one of
## namespaces/jforwarder-namespace.yaml to be there already

if [ -f "${OVERLAY_DIR}/namespace.yaml" ]; then
  kubectl apply -f "${OVERLAY_DIR}/namespace.yaml"
fi

## secrets are not part of the kustomize base: project/secrets holds dummies
## that secrets.sh replaces with the real ones, and kustomize would push the
## dummies over the real secrets in the cluster.

kubectl apply -f "${SCRIPT_DIR}/project/secrets" -n "${NAMESPACE}"

if [ -n "${PLAIN}" ]; then
  kubectl apply -f "${SCRIPT_DIR}/project/configmaps" -n "${NAMESPACE}"
  kubectl apply -f "${SCRIPT_DIR}/project/storage" -n "${NAMESPACE}"
  kubectl apply -f "${SCRIPT_DIR}/project/services" -n "${NAMESPACE}"
  kubectl apply -f "${SCRIPT_DIR}/project/statefulsets" -n "${NAMESPACE}"
  kubectl apply -f "${SCRIPT_DIR}/project/deployments" -n "${NAMESPACE}"
  kubectl apply -f "${SCRIPT_DIR}/project/ingress" -n "${NAMESPACE}"
  exit 0
fi

install_args=(-o "${ENVIRONMENT}")
[ -n "${USER_ARG}" ] && install_args+=(-u "${USER_ARG}")
[ -n "${REGISTRY_ARG}" ] && install_args+=(-H "${REGISTRY_ARG}")

# bash 3.2 (default on macos) treats an empty array as unset under `set -u`
"${KUSTOMIZE_DIR}/release.sh" install ${install_args[@]+"${install_args[@]}"}
