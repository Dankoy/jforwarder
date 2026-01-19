#!/bin/bash

## creates namespaces for development and production environments

monitoring/minio/minio.sh

## Setup mimir

kubectl apply -f monitoring/mimir/mimir-secret.yaml -n mimir
helm install mimir grafana/mimir-distributed -n mimir -f monitoring/mimir/values.yaml

sleep 100

## Setup loki 

helm install loki grafana/loki -f monitoring/loki/values.yaml -n monitoring

## Setup fluent

helm repo add fluent https://fluent.github.io/helm-charts
helm install fluent-operator fluent/fluent-operator -f monitoring/fluent-bit/fluent-operator.yaml -n fluent

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

kubectl apply -f monitoring/alertmanager/rules -n monitoring
kubectl apply -f monitoring/alertmanager/secrets/telegram-bot-token-secret.yaml -n monitoring
kubectl apply -f monitoring/alertmanager/secrets/telegram-bot-token-secret.yaml -n jforwarder
kubectl apply -f monitoring/alertmanager/secrets/telegram-bot-token-secret.yaml -n kafka
kubectl apply -f monitoring/alertmanager/secrets/telegram-bot-token-secret.yaml -n minio
kubectl apply -f monitoring/alertmanager/secrets/telegram-bot-token-secret.yaml -n mimir
kubectl apply -f monitoring/alertmanager/secrets/telegram-bot-token-secret.yaml -n minio-operator
kubectl apply -f monitoring/alertmanager/secrets/telegram-bot-token-secret.yaml -n kubernetes-dashboard
kubectl apply -f monitoring/alertmanager/receivers/telegram-receiver.yaml -n monitoring
kubectl apply -f monitoring/alertmanager/receivers/telegram-receiver.yaml -n jforwarder
kubectl apply -f monitoring/alertmanager/receivers/telegram-receiver.yaml -n kafka
kubectl apply -f monitoring/alertmanager/receivers/telegram-receiver.yaml -n minio
kubectl apply -f monitoring/alertmanager/receivers/telegram-receiver.yaml -n mimir
kubectl apply -f monitoring/alertmanager/receivers/telegram-receiver.yaml -n minio-operator
kubectl apply -f monitoring/alertmanager/receivers/telegram-receiver.yaml -n kubernetes-dashboard

kubectl apply -f monitoring/podmonitor -n monitoring
kubectl apply -f monitoring/servicemonitor -n monitoring
kubectl apply -f monitoring/zipkin -n monitoring

sleep 100









