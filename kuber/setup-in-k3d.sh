#!/bin/bash

## setup cluster

printf "\n------- Setting up Kubernetes cluster -------  \n\n"

k3d cluster create mycluster --config k3d/k3d-default.yaml

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

