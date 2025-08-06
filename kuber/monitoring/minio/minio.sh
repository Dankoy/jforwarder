#!/bin/bash

## creates namespaces for development and production environments

kubectl config use-context minio

kubectl apply -f monitoring/minio/minio-env-secret.yaml -n minio
kubectl apply -f monitoring/minio/minio-pv.yaml -n minio

helm repo add minio-operator https://operator.min.io
helm install \
  --namespace minio-operator \
  --create-namespace \
  operator minio-operator/operator

helm install \
--namespace minio \
--create-namespace \
--values monitoring/minio/values.yaml \
minio minio-operator/tenant



