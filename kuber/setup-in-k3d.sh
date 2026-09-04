#!/bin/bash

## setup cluster

OPTSTRING=":c:u:H:o:"

while getopts ${OPTSTRING} opt; do
  case ${opt} in
    c)
      printf "k3d cluster name: %s \n" "${OPTARG}"
      CLUSTER=${OPTARG}
      ;;
    u)
      printf "docker registry user: %s \n" "${OPTARG}"
      REGISTRY_USER=${OPTARG}
      ;;
    H)
      printf "docker registry host: %s \n" "${OPTARG}"
      REGISTRY_HOST=${OPTARG}
      ;;
    o)
      printf "environment: %s \n" "${OPTARG}"
      ENVIRONMENT=${OPTARG}
      ;;
    :)
      printf "Option -%s requires an argument. \n" "${OPTARG}"
      exit 1
      ;;
    ?)
      printf "Invalid option: -%s. \n" "${OPTARG}"
      exit 1
      ;;
  esac
done

if [ -z "$CLUSTER" ]; then
  echo "Error: Cluster name must be provided as a command-line argument with -c"
  exit 1
fi

## -u, -H and -o are passed to apply-all.sh, which deploys the project with
## kustomize. Without -u the images are taken as they are, which is what
## locally built k3d images need, without -o it is the production overlay.

APPLY_ARGS=()
if [ -n "${ENVIRONMENT:-}" ]; then
  APPLY_ARGS+=(-o "${ENVIRONMENT}")
fi
if [ -n "${REGISTRY_USER:-}" ]; then
  APPLY_ARGS+=(-u "${REGISTRY_USER}")
fi
if [ -n "${REGISTRY_HOST:-}" ]; then
  APPLY_ARGS+=(-H "${REGISTRY_HOST}")
fi


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

# bash 3.2 (default on macos) treats an empty array as unset
./apply-all.sh ${APPLY_ARGS[@]+"${APPLY_ARGS[@]}"}

printf "\n------- Jforwarder created ------- \n\n"



printf "\n------- Cluster setup complete ------- \n\n"

