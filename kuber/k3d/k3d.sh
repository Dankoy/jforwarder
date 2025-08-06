#!/bin/bash

## creates namespaces for development and production environments

kubectl apply -f namespaces/jforwarder-namespace.yaml
kubectl apply -f namespaces/kafka-namespace.yaml
kubectl apply -f namespaces/monitoring-namespace.yaml
kubectl apply -f namespaces/minio-namespace.yaml
kubectl apply -f namespaces/mimir-namespace.yaml

# kubectl config set-context jforwarder --namespace=jforwarder \
#   --cluster=k3d-mycluster \
#   --user=admin@k3d-mycluster

# kubectl config set-context kafka --namespace=kafka \
#   --cluster=k3d-mycluster \
#   --user=admin@k3d-mycluster

# kubectl config set-context monitoring --namespace=monitoring \
#   --cluster=k3d-mycluster \
#   --user=admin@k3d-mycluster

# kubectl config set-context minio --namespace=minio \
#   --cluster=k3d-mycluster \
#   --user=admin@k3d-mycluster

# kubectl config set-context mimir --namespace=mimir \
#   --cluster=k3d-mycluster \
#   --user=admin@k3d-mycluster