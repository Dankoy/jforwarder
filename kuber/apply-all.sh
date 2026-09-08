#!/bin/bash

## applies the whole project to the cluster with kustomize, which is the
## default deploy since #333:
##
##   cp .env.deploy.example .env.deploy   # once
##   ./apply-all.sh
##
## The script takes no arguments, every setting comes from .env.deploy: the
## environment, the docker hub user and the registry host. The image tag is
## not there, it lives in git in project/kustomize/overlays/<environment>
## and is bumped by "project/kustomize/release.sh version".

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
KUSTOMIZE_DIR="${SCRIPT_DIR}/project/kustomize"
ENV_FILE="${SCRIPT_DIR}/.env.deploy"

if [ $# -gt 0 ]; then
  printf "apply-all.sh takes no arguments, settings live in %s \n" "${ENV_FILE}"
  exit 1
fi

if [ ! -f "${ENV_FILE}" ]; then
  printf "%s not found, copy it from .env.deploy.example first \n" "${ENV_FILE}"
  exit 1
fi

# shellcheck source=/dev/null
. "${ENV_FILE}"

DOCKER_HUB_USER="${DOCKER_HUB_USER:-}"
REGISTRY_HOST="${REGISTRY_HOST:-docker.io}"
ENVIRONMENT="${ENVIRONMENT:-production}"
DEPLOY_MODE="${DEPLOY_MODE:-kustomize}"

if [ "${DEPLOY_MODE}" != "kustomize" ] && [ "${DEPLOY_MODE}" != "plain" ]; then
  printf "Unknown DEPLOY_MODE %s, expected kustomize or plain \n" \
    "${DEPLOY_MODE}"
  exit 1
fi

## release.sh owns the overlays: it validates ENVIRONMENT and reports the
## namespace its overlay declares, so that knowledge lives in one place.

OVERLAY_DIR="${KUSTOMIZE_DIR}/overlays/${ENVIRONMENT}"
NAMESPACE="$("${KUSTOMIZE_DIR}/release.sh" namespace)"

printf "\nDeploying %s to namespace %s \n\n" "${ENVIRONMENT}" "${NAMESPACE}"

## dev and test bring their own namespace, production expects the one of
## namespaces/jforwarder-namespace.yaml to be there already

if [ -f "${OVERLAY_DIR}/namespace.yaml" ]; then
  kubectl apply -f "${OVERLAY_DIR}/namespace.yaml"
fi

## secrets are not part of the kustomize base: project/secrets holds dummies
## that secrets.sh replaces with the real ones, and kustomize would push the
## dummies over the real secrets in the cluster.

kubectl apply -f "${SCRIPT_DIR}/project/secrets" -n "${NAMESPACE}"

if [ "${DEPLOY_MODE}" = "plain" ]; then
  kubectl apply -f "${SCRIPT_DIR}/project/configmaps" -n "${NAMESPACE}"
  kubectl apply -f "${SCRIPT_DIR}/project/storage" -n "${NAMESPACE}"
  kubectl apply -f "${SCRIPT_DIR}/project/services" -n "${NAMESPACE}"
  kubectl apply -f "${SCRIPT_DIR}/project/statefulsets" -n "${NAMESPACE}"
  kubectl apply -f "${SCRIPT_DIR}/project/deployments" -n "${NAMESPACE}"
  kubectl apply -f "${SCRIPT_DIR}/project/ingress" -n "${NAMESPACE}"
  exit 0
fi

"${KUSTOMIZE_DIR}/release.sh" install
