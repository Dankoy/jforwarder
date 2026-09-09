#!/bin/bash

## creates namespaces for development and production environments

if ! command -v helmfile > /dev/null; then
    echo "helmfile is not installed, see README" >&2
    exit 1
fi

kubectl apply -f monitoring/minio/minio-env-secret.yaml -n minio
kubectl apply -f monitoring/minio/minio-pv.yaml -n minio

## The operator and the tenant come from helmfile.yaml, which pins their chart
## versions and knows the tenant needs the operator first.

helmfile -l name=operator -l name=minio sync --concurrency 1



