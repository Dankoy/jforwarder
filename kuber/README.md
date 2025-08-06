# Kubernetes Deployment Guide

This guide provides a step-by-step process for deploying applications to Kubernetes using YAML

minikube works so bad so its usage strongly not advised.
microk8s doesn't work on macos and doesn't work on orangepi armbian.

Tried k3d as docker wrapper for k3s and it feels better than others.

# Minikube

## Kubelet configs

Create kubelet ![config](./kubelet-config.yaml) file and apply it whent minikube starts

```shell
minikube start --cpus 8 --memory 9000 --extra-config=kubelet.config=/kubelet-config.yaml
```

Something is wrong with config. Direct parameter change is working fine

```shell
 minikube start --cpus 8 --memory 9000 --extra-config=kubelet.runtime-request-timeout=10m
```

## Namespaces

Create namespaces for dev and prod using this [tutorial](https://kubernetes.io/docs/tutorials/cluster-management/namespaces-walkthrough/)

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: production
  labels:
    name: production
```

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: development
  labels:
    name: development

```

```shell
kubectl apply -f dev/namespace.yaml
kubectl apply -f prod/namespace.yaml

kubectl config view

kubectl config set-context dev --namespace=development \
  --cluster=minikube \
  --user=minikube

kubectl config set-context prod --namespace=production \
  --cluster=minikube \
  --user=minikube

kubectl config use-context dev

kubectl config current-context
```

## Restore postgres database from backup

First expose service in minikube

```shell
minikube service telegram-chat-service-postgres-db -n development
```

Then run pg_restore command to restore the database.

```shell
pg_restore --verbose --clean --no-acl --no-owner -h 127.0.0.1 -U postgres -p 49889 -d dev ~/pg_2025-07-30_01-00-01.sql -W
```

## Expose ingress

Enable addons ingress and ingress-dns

```shell
minikube addons enable ingress
minikube addons enable ingress-dns
```

Add in /etc/hosts necessary entries:

```shell
$(minikube ip) spring-eureka-registry
```

Then run tunnel (for mac only). For linux it should work as is

```shell
minikube tunnel
```

## Install kafka 

To install kafka I used strimzi operator. 

Guide [here](https://piotrminkowski.com/2023/11/06/apache-kafka-on-kubernetes-with-strimzi/)

```shell
helm install strimzi-cluster-operator --set strimzi.io/kraft=enabled  oci://quay.io/strimzi-helm/strimzi-kafka-operator -f helm/strizmi-kafka/strizmi-values.yaml
```

Kafka NodePools and cluster configurations is available in [helm/strizmi-kafka](./kafka/strizmi-kafka) directory. Apply it and everything should work fine.

Examples from strimzi could be find [here](https://github.com/strimzi/strimzi-kafka-operator/tree/main/examples)

---

# k3d

## start with ingress support

Loadbalancer ports (80) should be used that exists in ingress.yaml. 

So if there are multiple services then it should be added as multiple port bindings.

```shell
k3d cluster create mycluster -p "8081:80@loadbalancer"
```

Ingress should be available in http://127.0.0.1:8081

More [here](https://k3d.io/v5.3.0/usage/exposing_services/)

Better to use config files

```shell
k3d cluster create mycluster --config k3d-default.yaml
```

Apply ingress files to ports described in service as PORT (not target port).

HOST_PORT:SERVICE_PORT

Services should have clusterip type 

In etc hosts should be added hostnames to access services

## Install dashboard

```shell
helm repo add kubernetes-dashboard https://kubernetes.github.io/dashboard/

helm upgrade --install kubernetes-dashboard kubernetes-dashboard/kubernetes-dashboard --create-namespace --namespace kubernetes-dashboard
```

Apply service account for dashboard

```yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: admin-user
  namespace: kube-system
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: admin-user
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
- kind: ServiceAccount
  name: admin-user
  namespace: kube-system
```

Create token

```shell
kubectl -n kube-system create token admin-user
```

Port forward dashboard (not necessary for ingress)

```shell
kubectl port-forward -n kubernetes-dashboard service/kubernetes-dashboard-kong-proxy 10443:443 --address 0.0.0.0 &
```

Dashboard is available by https://localhost:10443/


Or it is better to use ingress.

Ingress should send http requests to kong-proxy which should accept it. It is configured by values.yaml.

Dashboard is accessible by https://kubernetes-dashboard:8443/

More info [here](https://medium.com/@tinhtq97/kubernetes-dashboard-7-x-unknown-error-200-a5be156db23f)


## Add namespaces and contexts

```shell
./k3d.sh
```

## Install kafka 

To install kafka I used strimzi operator. 

Guide [here](https://piotrminkowski.com/2023/11/06/apache-kafka-on-kubernetes-with-strimzi/)

```shell
helm install strimzi-cluster-operator --set strimzi.io/kraft=enabled  oci://quay.io/strimzi-helm/strimzi-kafka-operator -f helm/strizmi-kafka/strizmi-values.yaml -n kafka
```

Kafka NodePools and cluster configurations is available in [helm/strizmi-kafka](./kafka/strizmi-kafka) directory. Apply it and everything should work fine.

Examples from strimzi could be find [here](https://github.com/strimzi/strimzi-kafka-operator/tree/main/examples)


When trying to redeploy kafka, it is necessary to delete PVC, strimzi operator and then install it again and apply kafka node pool

## Install everything for project

```shell
./apply-all.sh
```

### Project deployments 

Project contains only template files for deployments. Also threre is a script file to generate deployment file dynamically.

```shell
./release.sh -u registry_user -r registry_host -t 1.8.0-SNAPSHOT
```

