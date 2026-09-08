#!/bin/bash

## setup cluster
##
## Takes no arguments, every setting comes from .env.deploy: the cluster name
## and, through apply-all.sh, the environment and the image coordinates.

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="${SCRIPT_DIR}/.env.deploy"

if [ $# -gt 0 ]; then
  printf "setup-in-k3d.sh takes no arguments, settings live in %s \n" \
    "${ENV_FILE}"
  exit 1
fi

if [ ! -f "${ENV_FILE}" ]; then
  printf "%s not found, copy it from .env.deploy.example first \n" "${ENV_FILE}"
  exit 1
fi

## .env.deploy is read as KEY=value and never sourced, see apply-all.sh

CLUSTER="$(sed -n "s/^[[:space:]]*K3D_CLUSTER=//p" "${ENV_FILE}" | tail -n1 \
  | tr -d '\r' | sed -e 's/[[:space:]]*#.*$//' -e 's/[[:space:]]*$//' \
        -e 's/^"\(.*\)"$/\1/' -e "s/^'\(.*\)'\$/\1/")"

if [ -z "${CLUSTER}" ]; then
  printf "K3D_CLUSTER is empty in %s \n" "${ENV_FILE}"
  exit 1
fi

printf "k3d cluster name: %s \n" "${CLUSTER}"

printf "\n------- Setting up Kubernetes cluster -------  \n\n"

k3d cluster create "$CLUSTER" --config k3d/k3d-default.yaml

sleep 30 # wait for cluster to be ready

printf "\n------- Kubernetes cluster created and ready ------- \n\n"

## Add repos

printf "\n------- Adding repos ------- \n\n"

helm repo add kubernetes-dashboard https://kubernetes.github.io/dashboard/

helm repo add strimzi https://strimzi.io/charts/

helm repo add prometheus-community \
    https://prometheus-community.github.io/helm-charts

helm repo add grafana https://grafana.github.io/helm-charts

printf "\n------- Adding repos complete ------- \n\n"

sleep 30

## setup cluster namespaces

printf "\n------- Setup namespaces ------- \n\n"

./k3d/k3d.sh

printf "\n------- Namespaces created  ------- \n\n"

## setup storage

printf "\n------- Setup storage ------- \n\n"

kubectl apply -f storage

printf "\n------- Storage created ------- \n\n"

## setup dashboard

printf "\n------- Setup dashboard ------- \n\n"

./dashboard/dashboard.sh

sleep 30

printf "\n------- Dashboard created ------- \n\n"

## apply monitoring

printf "\n------- Setup monitoring ------- \n\n"

./monitoring/apply-all.sh

sleep 60

printf "\n------- Monitoring created ------- \n\n"

## apply kafka with strimzi

printf "\n------- Setup kafka ------- \n\n"

./apply-kafka.sh

sleep 60

printf "\n------- Kafka created ------- \n\n"

## apply full project

printf "\n------- Setup jforwarder project ------- \n\n"

./apply-all.sh

printf "\n------- Jforwarder created ------- \n\n"



printf "\n------- Cluster setup complete ------- \n\n"

