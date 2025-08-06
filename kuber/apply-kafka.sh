#!/bin/bash

## creates namespaces for development and production environments

helm repo add strimzi https://strimzi.io/charts/
helm install strimzi-cluster-operator --set strimzi.io/kraft=enabled  strimzi/strimzi-kafka-operator -f kafka/strizmi-kafka/strizmi-values.yaml -n kafka

kubectl apply -f kafka/strizmi-kafka -n kafka
