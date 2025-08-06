#!/bin/bash

## dashboard

helm repo add kubernetes-dashboard https://kubernetes.github.io/dashboard/

helm upgrade --install kubernetes-dashboard \
kubernetes-dashboard/kubernetes-dashboard \
--create-namespace --namespace kubernetes-dashboard \
-f dashboard/values.yaml

kubectl apply -f dashboard/serviceaccounts/dashboard-account.yaml

kubectl -n kube-system create token admin-user

#sleep 45

#kubectl port-forward -n kubernetes-dashboard service/kubernetes-dashboard-kong-proxy 10443:443 --address 0.0.0.0 &

kubectl apply -f dashboard/ingress/ingress.yaml


