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

## .env.deploy is read as KEY=value and never sourced: a stray SCRIPT_DIR or a
## typo in it would otherwise be executed and quietly move the script around.
## Inline comments, surrounding quotes and CRLF endings are stripped, and a
## value therefore cannot contain a "#".

setting() {
  sed -n "s/^[[:space:]]*$1=//p" "${ENV_FILE}" | tail -n1 | tr -d '\r' \
    | sed -e 's/[[:space:]]*#.*$//' -e 's/[[:space:]]*$//' \
          -e 's/^"\(.*\)"$/\1/' -e "s/^'\(.*\)'\$/\1/"
}

ENVIRONMENT="$(setting ENVIRONMENT)"
ENVIRONMENT="${ENVIRONMENT:-production}"
DEPLOY_MODE="$(setting DEPLOY_MODE)"
DEPLOY_MODE="${DEPLOY_MODE:-kustomize}"

if [ "${DEPLOY_MODE}" != "kustomize" ] && [ "${DEPLOY_MODE}" != "plain" ]; then
  printf "Unknown DEPLOY_MODE %s, expected kustomize or plain \n" \
    "${DEPLOY_MODE}"
  exit 1
fi

## The plain manifests know nothing about environments: project/storage holds
## the hostPath PersistentVolumes, which are cluster scoped and named the same
## for everyone, so applying them for dev or test takes the volumes of
## production with them.

if [ "${DEPLOY_MODE}" = "plain" ] && [ "${ENVIRONMENT}" != "production" ]; then
  printf "DEPLOY_MODE=plain is production only, ENVIRONMENT is %s \n" \
    "${ENVIRONMENT}"
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
