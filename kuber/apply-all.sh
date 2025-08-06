#!/bin/bash

## creates namespaces for development and production environments

kubectl config use-context jforwarder

kubectl apply -f project/configmaps -n jforwarder
kubectl apply -f project/storage -n jforwarder
kubectl apply -f project/secrets -n jforwarder
kubectl apply -f project/services -n jforwarder
kubectl apply -f project/statefulsets -n jforwarder
kubectl apply -f project/deployments -n jforwarder
kubectl apply -f project/ingress -n jforwarder
