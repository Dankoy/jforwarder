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
[helmfile.yaml](./helmfile.yaml), because the manifests are written for the CRD
version that chart ships (`kafka.strimzi.io/v1`) and for the kafka versions its
operator supports.

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

## Charts

Every chart of the cluster - the strimzi operator, the monitoring stack, minio -
is declared in [helmfile.yaml](./helmfile.yaml) with its **version pinned**, its
namespace and its values file. The scripts do not call `helm` any more, they
call `helmfile` with a selector:

```shell
helmfile diff                                    # what would change
helmfile apply                                   # every chart
helmfile -l name=strimzi-cluster-operator apply   # one of them, as apply-kafka.sh does
```

`helmfile` has to be installed (`brew install helmfile`, or the
`ghcr.io/helmfile/helmfile` image); repositories are added by it, so the scripts
no longer do that either.

Pinning is the point. An unpinned `helm install` takes whatever is latest on the
day it runs, which is how the kafka manifests ended up two API versions behind
their operator (#356). Raising a version is now a diff in git:

```shell
$EDITOR helmfile.yaml       # version: 1.2.0 -> 1.3.0
helmfile diff               # read what it changes in the cluster
helmfile apply
git commit -am "chore: strimzi 1.3.0"
```

A pin is either the version the cluster runs or a version someone deliberately
raised it to; it is never "whatever was newest that day". A deploy is not the
place to find out that a chart moved thirteen major versions ahead.

| release | pinned | cluster runs | note |
| --- | --- | --- | --- |
| strimzi-kafka-operator | 1.2.0 | 0.47.0 | the v1 API migration above (#356) |
| minio operator, tenant | 7.1.1 | 7.1.1 | latest |
| mimir-distributed | 6.2.0 | 5.8.0 | new architecture, see below |
| loki | 7.3.0 | 6.38.0 | CRDs by hand first |
| fluent-operator | 4.3.0 | 3.5.0 | CRDs by hand first |
| kube-prometheus-stack | 90.0.0 | 77.1.0 | CRDs by hand first |

Each of the raised charts was checked by rendering it at the old and the new
version against this repository's own values file and diffing the result. What
moves:

* **loki 7.3.0** (loki 3.5.3 -> 3.6.12) renders the same set of objects, but it
  needed three edits in [loki/values.yaml](./monitoring/loki/values.yaml), all
  found by running it, not by reading the diff - see below;
* **fluent-operator 4.3.0** (fluent-bit operator 3.10.0) drops the
  `docker:20.10` init container that wrote `fluent-bit.env` and ships that file
  as a ConfigMap instead, and it gives the operator a non-root securityContext
  and liveness and readiness probes. Same CRD names as 3.5.0, different schemas;
* **kube-prometheus-stack 90.0.0** brings prometheus-operator 0.93.1,
  prometheus 3.14.0, grafana 13.2.1 and alertmanager 0.34.0, most of them on
  distroless images now. The grafana test Pod is gone, the `kube-webhook-certgen`
  image moved to `ghcr.io/jkroepke`. Every datasource, the derived fields and
  the `remoteWrite` of [kubestack-values.yaml](./monitoring/kubestack-values.yaml)
  render unchanged;
* **mimir-distributed 6.2.0** (mimir 2.17.0 -> 3.2.0) is the one that is not
  just a bump - it changes the write path and renames a service, and it has its
  own section below.

Raising a version is its own commit, and for a chart that brings CRDs it is two
steps, because **helm never updates CRDs on upgrade** - they are installed once:

```shell
$EDITOR helmfile.yaml                        # version: 5.8.0 -> 6.2.0
helm show crds <repo>/<chart> --version 6.2.0 | kubectl apply --server-side -f -
helmfile -l name=<release> diff              # read what changes
helmfile -l name=<release> sync
git commit -am "chore: mimir 6.2.0"
```

Charts whose CRDs sit in a subchart - kube-prometheus-stack keeps them in
`charts/crds/crds` - do not answer `helm show crds`; pull the chart and apply
that directory instead.

### Applying the pins that are ahead

Three of the four need CRDs applied before the sync, and *which* CRDs matters -
see the warning under the block. Run them one at a time and read the diff
before the sync.
The order is not free: kube-prometheus-stack goes last, because the mimir URLs
it carries only resolve once mimir has been synced under its new service name -
read the mimir section below before starting.

**One thing to do before starting.** Loki now reads its tenant credentials from
a `loki-secret` in the `monitoring` namespace, which never existed before - see
the loki section below for why. Without it loki comes up, reports ready and
fails every S3 call, so put it in first:

```shell
$EDITOR .all_secrets/monitoring/loki/loki-secret.yaml   # ACCESS_KEY_ID, SECRET_ACCESS_KEY
./secrets.sh
kubectl apply -f monitoring/loki/loki-secret.yaml -n monitoring
```

`.all_secrets/monitoring/loki/` is a new directory - the store has no `loki`
folder yet, so create it before writing the file. `secrets.sh` runs without
`set -e`, so a `cp` from a path that is not there says so and carries on, and
the `kubectl apply` that follows then refuses the untouched `${base64}`
placeholder. That is the tracked dummy doing its job rather than a failure to
debug, but it does mean the error you see is one step removed from the cause.

Any key that can read and write the `loki-*` buckets will do, including the one
mimir already uses - nothing has to be created in minio for this. A separate,
narrower user is a reasonable thing to want, and the tenant can mint one
(`tenant.users` in [minio/tenant-values.yaml](./monitoring/minio/tenant-values.yaml)),
but that is a decision about privileges and not part of this upgrade.

```shell
helm repo update grafana fluent prometheus-community

# loki needs no CRDs at all here - see the warning below
helmfile -l name=loki diff && helmfile -l name=loki sync

helm show crds fluent/fluent-operator --version 4.3.0 \
    | kubectl apply --server-side --force-conflicts -f -
helmfile -l name=fluent-operator diff && helmfile -l name=fluent-operator sync

# only the rollout-operator CRDs, not everything the chart carries
helm pull grafana/mimir-distributed --version 6.2.0 --untar
kubectl apply --server-side --force-conflicts \
    -f mimir-distributed/charts/rollout-operator/charts/crds/crds/
helmfile -l name=mimir diff && helmfile -l name=mimir sync

# subchart CRDs, so this one is pulled rather than shown. It is also what moves
# the grafana datasource and the prometheus remoteWrite to mimir-gateway.
helm pull prometheus-community/kube-prometheus-stack --version 90.0.0 --untar
kubectl apply --server-side --force-conflicts \
    -f kube-prometheus-stack/charts/crds/crds/
helmfile -l name=kube-prometheus-stack diff
helmfile -l name=kube-prometheus-stack sync
```

`--force-conflicts` is needed for the same reason as in the strimzi upgrade
above: helm owns those fields and a plain server-side apply is refused.

**Do not pipe `helm show crds` for loki or mimir into that apply.** Both charts
carry a grafana-agent-operator subchart, and its `crds/` directory contains
`servicemonitors`, `podmonitors` and `probes` of `monitoring.coreos.com` - the
same CRDs kube-prometheus-stack owns, at an ancient schema: 435 lines against
the 1429 that operator 0.93.1 ships. Applied with `--force-conflicts` they
replace the real ones, and every ServiceMonitor in the cluster is then read
through a schema that does not know most of its fields until the
kube-prometheus-stack step at the end puts them back. Neither release needs
any of it: with the values in this repository loki creates no custom resource
and deploys no operator, and mimir needs exactly the two
`rollout-operator.grafana.com` CRDs that the command above applies on their own.

**mimir 6.2.0 requires Kubernetes 1.32 or newer.** Its `kubeVersion` went from
`^1.20.0-0` to `^1.32.0-0`, and helm refuses the release rather than warning, so
an older cluster fails on the third step with the first two already applied.
Check `kubectl version` before starting.

Between the mimir sync and the kube-prometheus-stack sync the old `mimir-nginx`
service is already gone and prometheus still writes to it, so remote write
stalls for as long as those two steps are apart. It does not announce itself:
`prometheus_remote_storage_samples_failed_total` stays at 0 and only
`..._samples_retried_total` climbs - on the run this was written from it reached
50k with the queue about 100s behind. Nothing is lost, the queue drains to zero
once the stack is synced, but do not stop half way and do not read the retries
as damage.

Several pods also sit in `Terminating` for a long time, and none of it is a
hang - these components drain on shutdown and their grace periods say so:

| pod | terminationGracePeriodSeconds |
| --- | --- |
| prometheus | 600 |
| mimir query-scheduler | 180 |
| mimir distributor | 100 |

Prometheus is the one that catches people out: up to ten minutes of
`1/2 Terminating` while it flushes its WAL, with nothing in the events to say
that is what it is doing. Check `deletionTimestamp` plus
`deletionGracePeriodSeconds` before concluding anything is stuck.

The mimir distributor, ruler and one ingester crashloop for a minute or two
right after the mimir sync: kafka is still starting and they fail on
`dial tcp ...:9092: connection refused`, then come up on their own after two or
three restarts. Only worry if it is still happening once `mimir-kafka-0` is
`1/1 Running`.

Worth checking once it is all through, the four things that actually broke or
nearly broke during testing:

```shell
# remote write reaches the new service and has caught up
kubectl -n monitoring port-forward svc/kube-prometheus-stack-prometheus 9090:9090 &
curl -sG localhost:9090/api/v1/query --data-urlencode \
  'query=prometheus_remote_storage_highest_timestamp_in_seconds - ignoring(url,remote_name) prometheus_remote_storage_queue_highest_sent_timestamp_seconds'

# the gateway really is on the new version and nothing is left Pending
kubectl -n monitoring get pods,rs -l app.kubernetes.io/component=gateway

# loki got the credentials rather than the literal ${ACCESS_KEY_ID}
kubectl -n monitoring logs loki-0 -c loki | grep -c "operation error S3"
kubectl -n monitoring get pod loki-0 \
  -o jsonpath='{.spec.containers[?(@.name=="loki")].args}{"\n"}'   # expects -config.expand-env=true

# the ingest topic exists with the partition count from values.yaml
kubectl -n mimir logs deploy/mimir-distributor | grep "created Kafka topic"
```

### loki 6.38.0 -> 7.3.0

The rendered objects are the same and the chart takes every value the
repository sets, so the diff looks harmless. It is not: on a cluster, 7.3.0
fails two S3 paths that 6.38.0 serves, and both needed a values edit.

**The chunks bucket loses its name.** 7.x stops writing
`common.storage.s3.bucketnames` as soon as `loki.storage_config.aws.bucketnames`
is set - the chart treats the second as the authority and leaves the first
empty. Loki then sends `ListObjectsV2` with an empty bucket and minio answers
400, so the index never syncs and nothing is written. `bucketnames` is
therefore gone from `storage_config.aws`, leaving `storage.bucketNames.chunks`
as the one place the bucket is named.

**`insecure: true` stops being tolerated.** The endpoint is `https://minio.minio:443`
and `loki.storage.s3.insecure` was `true`, which means "speak plain HTTP" - a
contradiction loki 3.5.3 ignored and 3.6 does not: the ruler's client sends
HTTP to the TLS port, minio resets the connection, and the ruler logs
`unable to list rules ... StatusCode: 400` forever. It is now `false`; the
self-signed certificate is handled by `http_config.insecure_skip_verify`, which
is what that setting was always for.

**The gateway Deployment deadlocks on every upgrade.** This one is not about
7.3.0 at all - 6.38.0 renders the same thing - but it is what an upgrade hits
and a fresh install never does. The gateway is one replica, the chart gives it
a *required* `podAntiAffinity` on `kubernetes.io/hostname`, and this cluster has
one node. The default RollingUpdate starts the new pod before draining the old
one, the old pod's own rule keeps the new one off the only node, and it sits in
`Pending` indefinitely while helm reports the release upgraded and the gateway
quietly keeps serving the previous version. Deleting the pending pod does not
help: the old ReplicaSet is still scaled to 1 and wins the race again.

`gateway.deploymentStrategy` in [loki/values.yaml](./monitoring/loki/values.yaml)
now sets `maxSurge: 0` with `maxUnavailable: 1`, which drains before it starts
and costs a few seconds of gateway downtime. Two things that look like the
answer and are not:

* `gateway.affinity: {}` changes nothing. Helm merges maps, so an empty one
  leaves the chart's default in place; only `null` would drop it - and dropping
  it on the new pod still does not help, because it is the *old* pod's rule
  that does the blocking;
* `type: Recreate` cannot be applied to a Deployment that already exists. Helm's
  server-side apply merges, the `spec.strategy.rollingUpdate` block of the
  running object survives, and the API rejects the result with
  `may not be specified when strategy type is 'Recreate'`.

With all three edits 7.3.0 runs clean against the tenant. None of them shows up
in `helmfile diff` or in a rendered-manifest comparison - the config is valid
YAML either way, and only the object store and the scheduler reject it.

**Separately, and not caused by the upgrade:** the `${ACCESS_KEY_ID}` and
`${SECRET_ACCESS_KEY}` in this values file were never expanded. Loki only
substitutes environment variables when it is started with
`-config.expand-env=true` *and* given the secret that holds them, and neither
was set, so the config reached loki with those two strings literally and minio
answered `InvalidAccessKeyId`. 6.38.0 does the same, so this had been true for
as long as the file looked like that. mimir is the contrast: its chart renders
both, which is why the same `${...}` style always worked there.

Both go on the component that actually runs. With `deploymentMode: SingleBinary`
that is `singleBinary`, not `global` - the commented-out `global.extraEnvFrom`
at the top of the file would not have helped, which is easy to miss because the
key exists and helm accepts it silently. So:

```yaml
singleBinary:
  extraArgs:
    - -config.expand-env=true
  extraEnvFrom:
    - secretRef:
        name: loki-secret
```

with a `loki-secret` in the `monitoring` namespace carrying `ACCESS_KEY_ID` and
`SECRET_ACCESS_KEY`. That is in the values file now, and loki reaches the tenant
with it: no S3 errors, chunks and a compacted index written to `loki-chunks`.

### mimir 5.8.0 -> 6.2.0

This one is not a version bump, it is a change of architecture, and it needs
edits in two more files besides `helmfile.yaml`. They are already in this
commit; what follows is why each is there.

**The write path now goes through a kafka.** 6.x turns on the ingest-storage
architecture: the distributor writes every sample to a kafka topic and the
ingesters read it back, and `ingester.push_grpc_method_enabled` is `false`, so
there is no direct gRPC push left to fall back to. The chart brings that kafka
itself as a `mimir-kafka` StatefulSet (`kafka.enabled` defaults to `true`) and
mimir creates the `mimir-ingest` topic on start up. It has nothing to do with
the strimzi cluster in the `kafka` namespace, which belongs to the application.

Two of its defaults do not suit this cluster and
[mimir/values.yaml](./monitoring/mimir/values.yaml) overrides them: the pod asks
for a whole cpu, which nothing else here does, and the topic is created with 100
partitions, which is the chart's demo value. The only rule about partitions is
that there are no fewer than the maximum number of ingester replicas - there are
two - so it is set to 8. Raising it later means recreating the topic.

**`mimir-nginx` is now `mimir-gateway`.** Both mimir URLs in
[kubestack-values.yaml](./monitoring/kubestack-values.yaml) - the grafana
datasource and the prometheus `remoteWrite` - follow the rename here. Without
that edit grafana and `remoteWrite` fail the moment the release is synced, and
they fail quietly: prometheus keeps scraping and only the remote write queue
backs up.

**Three values keys were dropped by the chart** and are gone from
`mimir/values.yaml`: `nginx` (replaced by the `gateway` block, which was already
in the file with the same numbers and was inert until now), and `admin_api` and
`admin-cache`, which were Grafana Enterprise Metrics keys. Helm ignores unknown
keys silently, so they would have sat there looking effective.

**The rollout-operator now installs four admission webhooks**
(`prepare-downscale-mimir`, `no-downscale-mimir`, `pod-eviction-mimir`,
`zpdb-validation-mimir`). They are cluster-scoped objects but their
`namespaceSelector` is `kubernetes.io/metadata.name: mimir`, and they carry
`failurePolicy: Fail`: while the rollout-operator is down, StatefulSet updates
and pod evictions **in the mimir namespace** are refused. Deleting the release
does not delete them, so a `helm uninstall` leaves them behind to block the next
install.

**Two CRDs are new**, `replicatemplates` and
`zoneawarepoddisruptionbudgets`, both `rollout-operator.grafana.com`. Helm does
not install CRDs on an upgrade even when they did not exist before, so these
have to go in by hand - and only these two. Take them from the pulled chart's
`charts/rollout-operator/charts/crds/crds/`, never from `helm show crds`, which
would drag the grafana-agent-operator's copies of the kube-prometheus-stack
CRDs along with them.

Metrics already in the tenant are unaffected - the blocks in minio do not change
format - but anything still in an ingester's WAL when it restarts is at risk, as
it is on any ingester restart.

### helm 4 and charts that carry their own CRDs

On a cluster where the CRDs do not exist yet, kube-prometheus-stack fails on the
first install and succeeds on the second: helm 4 does not pick up the CRDs it
just installed while building the objects of the same release. This is not
about helmfile or about the chart version - plain `helm install` behaves the
same, on chart versions 65, 75, 86 and 90, while helm 3.16 installs the same
chart in one go. `monitoring/apply-all.sh` therefore runs that release twice;
the second run is a no-op once the CRDs are in place.

### Order matters, and nothing tells you when it is wrong

Both loki and mimir keep their data in the minio tenant, so they declare it in
`needs`. That dependency is quiet when it is not met: installed without the
tenant, loki starts, reports `2/2 Running` and **ready**, and only its log shows
that every S3 call fails with `no such host`; mimir does not even start, because
the endpoint and the bucket come from `mimir-secret`.

A selector skips `needs` - `--skip-needs` defaults to true whenever `-l` is
given - so `monitoring/apply-all.sh` passes `--include-needs`, and the tenant is
brought up first even when only mimir or loki is asked for:

```shell
helmfile -l name=mimir diff                    # mimir alone
helmfile -l name=mimir diff --include-needs    # minio operator, tenant, then mimir
```

The kubernetes dashboard release is in the file but disabled: the chart
repository it used to come from answers 404 and the project has not settled on a
new location, so `dashboard/dashboard.sh` is left as it was. Enable the release
and fill in its chart and version once upstream has a working source.

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
