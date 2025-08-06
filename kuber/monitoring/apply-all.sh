#!/bin/bash

## creates namespaces for development and production environments

monitoring/minio/minio.sh

## Setup mimir

kubectl apply -f monitoring/mimir/mimir-secret.yaml -n mimir
helm install mimir grafana/mimir-distributed -n mimir -f monitoring/mimir/values.yaml

sleep 100

## Setup loki 

helm install loki grafana/loki -f monitoring/loki/values.yaml -n monitoring

## Actual monitoring

sleep 60

kubectl apply -f monitoring/grafana/dashboards -n monitoring
kubectl apply -f monitoring/grafana/secrets -n monitoring
kubectl apply -f monitoring/ingress/ingress.yaml -n monitoring

helm repo add prometheus-community \
    https://prometheus-community.github.io/helm-charts

helm install kube-prometheus-stack \
    prometheus-community/kube-prometheus-stack \
    -n monitoring --create-namespace -f monitoring/kubestack-values.yaml

kubectl apply -f monitoring/podmonitor -n monitoring
kubectl apply -f monitoring/servicemonitor -n monitoring
kubectl apply -f monitoring/zipkin -n monitoring

sleep 100









