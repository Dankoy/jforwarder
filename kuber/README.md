# Kubernetes Deployment Guide

This guide provides a step-by-step process for deploying applications to Kubernetes using YAML

minikube works so bad so its usage strongly not advised.
microk8s doesn't work on macos and doesn't work on orangepi armbian.

Tried k3d as docker wrapper for k3s and it feels better than others.

# Minikube

## Kubelet configs

Create kubelet ![config](./kubelet-config.yaml) file and apply it when minikube starts

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

## k3d remote access

To access k3d cluster from remote machine, you have to use ssh tunneling mechanism. Because k3d doesn't allow users to access it remotely. Tried to change hostIP in k3d config file to 0.0.0.0 and give it a hostname. Nothing worked. 

So to access k3d cluster from remote it is necessary:

Write in /etc/hosts file (same stuff as for local cluster): 

```text
127.0.0.1 grafana
127.0.0.1 zipkin
127.0.0.1 kubernetes-dashboard
```

Then make ssh tunnel to host:

```shell
ssh -L 8888:localhost:8443 user@remote_ip
```

where 
8888 - port on local machine
8443 - port on remote machine

Then it should be possible to connect to dashboard, grafana and etc in browser with `https://grafana:8888/`and `https://kubernetes-dashboard:8888/`


## Remote access with kubectl 

Same stuff. Do ssh tunneling, but for port 6445


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
./apply-kafka.sh
```

The script installs the operator and applies the manifests of
[kafka/strizmi-kafka](./kafka/strizmi-kafka): the `Kafka` cluster, its
`KafkaNodePool`, the schema registry and the UI. The chart version is pinned in
the script, because the manifests are written for the CRD version that chart
ships (`kafka.strimzi.io/v1`) and for the kafka versions its operator supports.

Examples from strimzi could be find [here](https://github.com/strimzi/strimzi-kafka-operator/tree/main/examples)

When trying to redeploy kafka, it is necessary to delete PVC, strimzi operator and then install it again and apply kafka node pool

### Upgrading strimzi on a cluster that already runs it

On a fresh cluster `./apply-kafka.sh` is enough. On a cluster that already runs
an older operator it is not: the chart is pinned to 1.2.0, whose CRDs serve
`kafka.strimzi.io/v1` only, and getting there from a `v1beta2` cluster is a
migration, not an install. The steps below were run end to end on a k3d cluster
that started at strimzi 0.46.1 with kafka 4.0.0.

Two things make it awkward, and both are silent:

* helm ships strimzi CRDs in the chart's `crds/` directory, and helm installs
  those **once and never upgrades them**. `helm upgrade` replaces the operator
  and leaves the CRDs where they were, so `kubectl wait` passes - the CRDs do
  exist - and the apply right after it fails with
  `no matches for kind "Kafka" in version "kafka.strimzi.io/v1"`;
* the `v1beta2` version cannot simply be dropped from a CRD. Kubernetes refuses
  with `status.storedVersions[0]: Invalid value: "v1beta2": missing from
  spec.versions` until every stored resource has been rewritten as `v1`.

**1. Upgrade to an intermediate version that serves both APIs.** `v1` appears
in 0.50 and 0.51 next to `v1beta2`; 1.x serves `v1` alone. The CRDs have to be
applied by hand, and `--force-conflicts` is needed because helm owns those
fields - without it the apply is refused and nothing changes:

```shell
helm repo add strimzi https://strimzi.io/charts/
helm repo update strimzi
helm show crds strimzi/strimzi-kafka-operator --version 0.51.0 \
    | kubectl apply --server-side --force-conflicts -f -
helm upgrade strimzi-cluster-operator strimzi/strimzi-kafka-operator \
    --version 0.51.0 -f kafka/strizmi-kafka/strizmi-values.yaml -n kafka
```

While the cluster sits here with kafka 4.0.0 its `Kafka` resource may report
`Unsupported Kafka.spec.kafka.version: 4.0.0` - 0.51 no longer supports it. The
version moves in step 3, so do not stop half way.

**2. Convert the resources and the CRDs with the strimzi tool.** It is in the
[1.0.0 release](https://github.com/strimzi/strimzi-kafka-operator/releases/tag/1.0.0)
as `strimzi-v1-api-conversion-1.0.0.tar.gz` and needs java:

```shell
bin/v1-api-conversion.sh convert-resource --all-namespaces
bin/v1-api-conversion.sh crd-upgrade
```

`convert-resource` rewrites every strimzi custom resource in the `v1` schema,
`crd-upgrade` makes `v1` the stored version, touches the resources so nothing
stays persisted as `v1beta2`, and removes `v1beta2` from `status.storedVersions`.
After it `kubectl get crd kafkas.kafka.strimzi.io -o jsonpath='{.status.storedVersions}'`
prints `["v1"]`.

**3. Apply the pinned CRDs and run the script:**

```shell
helm show crds strimzi/strimzi-kafka-operator --version 1.2.0 \
    | kubectl apply --server-side --force-conflicts -f -
./apply-kafka.sh
```

The operator takes it from there: in the test it rolled the broker from kafka
4.0.0 to the 4.3.1 of `kafka-one-node.yaml` and moved the metadata version to
4.3-IV0 by itself, in one reconciliation, without a separate step for the
metadata version.

**4. Check:**

```shell
kubectl get kafka -n kafka          # READY True, 4.3.1, 4.3-IV0
kubectl get pods -n kafka
kubectl exec -n kafka my-cluster-dual-role-0 -- \
    bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
```

The data survives: the node pool keeps `deleteClaim: false`, so the broker PVC
outlives the operator upgrade and the restart. In the test the topics, their
offsets and a message written before the upgrade were all still there
afterwards, and the consumer reconnected on its own and caught up to zero lag.

## Install everything for project

Deploy of the project is done with kustomize since #333. Every setting lives in
`kuber/.env.deploy`, the scripts take no arguments:

```shell
cd kuber
cp .env.deploy.example .env.deploy   # once, the copy is gitignored
./secrets.sh
./apply-all.sh
```

```shell
DOCKER_HUB_USER=      # empty for locally built k3d images
REGISTRY_HOST=docker.io
ENVIRONMENT=production   # or dev, test
DEPLOY_MODE=kustomize    # or plain, the pre kustomize flow, production only
K3D_CLUSTER=my-cluster   # used by setup-in-k3d.sh
```

`apply-all.sh` applies `project/secrets` and hands everything else to
[project/kustomize](./project/kustomize). The image tag is not in that file, it
lives in git in `project/kustomize/overlays/<environment>`; the registry user is
read at deploy time and never committed.

dev and test bring their own namespace (`jforwarder-dev`, `jforwarder-test`) and
take the database volumes from the `local-path` provisioner instead of the
hostPath `PersistentVolume`s, so they can live in the same cluster as
production.

### Project deployments with kustomize

[project/kustomize](./project/kustomize) is what `apply-all.sh` uses. The
deployed version is bumped in git, the way `PROJECT_VERSION` is bumped in
`build.gradle`, and a release is a committed diff of one overlay followed by
one command:

```shell
cd project/kustomize
./release.sh version                      # tag of build.gradle into the overlay
git diff overlays                         # review, commit - that is the release
./release.sh render | kubectl diff -f -   # what will change in the cluster
./release.sh install                      # kubectl apply -k
```

`apply-all.sh` is for the first deploy of an environment: it also applies
`project/secrets`, which are dummies until `secrets.sh` has replaced them, so
running it on a machine without `.all_secrets` overwrites the real secrets in
the cluster. A release needs `release.sh install` and nothing else.

The full sequence, the rollback and the case of several environments are in
[project/kustomize/README.md](./project/kustomize/README.md#releasing).

Deleting is manual: `kubectl apply -k` never removes anything, so a service
dropped from git keeps running until `kubectl delete` is run for it. See
[project/kustomize/README.md](./project/kustomize/README.md) for the details
and for why automatic pruning is not wired in.

### Project deployments with sed templates (legacy)

The flow that was default before kustomize. `project/deployments` contains only
template files, `release.sh` generates the deployments from them with `sed` and
`apply-all.sh` applies the plain folders when `DEPLOY_MODE=plain`:

```shell
./release.sh -u registry_user -H registry_host -t 1.8.0-SNAPSHOT
./apply-all.sh   # with DEPLOY_MODE=plain in .env.deploy
```

### Project deployments with helm

The same project resources are also packaged as a helm chart in
[project/helm](./project/helm). It replaces `release.sh` + `apply-all.sh` with a
single command and adds `helm history` / `helm rollback`:

The wrapper keeps the two steps of the old flow: `version` substitutes the image
coordinates (like `release.sh` did), `install` applies the chart (like
`apply-all.sh` did).

```shell
cd project/helm
./release.sh version -u registry_user -t 1.8.0-SNAPSHOT
./release.sh install -f values-secrets.yaml
```

See [project/helm/README.md](./project/helm/README.md) for the values reference.

## Cleanup images

To cleanup unused images inside k3d kuber cluster do

```shell
docker exec {k3d_server_name} sh -c "crictl rmi --prune"
```
