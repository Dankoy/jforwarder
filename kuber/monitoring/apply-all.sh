#!/bin/bash

## creates namespaces for development and production environments

if ! command -v helmfile > /dev/null; then
    echo "helmfile is not installed, see README" >&2
    exit 1
fi

monitoring/minio/minio.sh

## Setup mimir

kubectl apply -f monitoring/mimir/mimir-secret.yaml -n mimir

## loki reads the tenant credentials out of this one. Its values file names it
## in singleBinary.extraEnvFrom and starts loki with -config.expand-env=true,
## which is what turns the ${ACCESS_KEY_ID} in that file into a real key.

kubectl apply -f monitoring/loki/loki-secret.yaml -n monitoring

## mimir, loki and the fluent operator come from helmfile.yaml, which pins their
## chart versions. Both mimir and loki store in the minio tenant and declare it
## in "needs"; a selector skips needs unless --include-needs is given, and
## without the tenant they start, report ready and fail every S3 call.
##
## "sync" and not "apply": apply runs helm-diff first, and a chart that brings
## its own CRDs and objects of those kinds - fluent-operator here - cannot be
## rendered while the cluster does not know them yet. See
## https://github.com/roboll/helmfile/issues/1353

helmfile -l name=mimir -l name=loki -l name=fluent-operator sync \
    --include-needs --concurrency 1

## Custom multiline parser referenced from fluent-operator.yaml. Needs the
## CRDs the chart above installs, so wait for them to be established first.

kubectl wait --for condition=established --timeout=60s \
    crd/clustermultilineparsers.fluentbit.fluent.io
kubectl apply -f monitoring/fluent-bit/multiline-parser-springboot.yaml
kubectl apply -f monitoring/fluent-bit/loglevel-filter.yaml

## Actual monitoring

sleep 60

kubectl apply -f monitoring/grafana/dashboards -n monitoring
kubectl apply -f monitoring/grafana/secrets -n monitoring
kubectl apply -f monitoring/ingress/ingress.yaml -n monitoring

## The chart keeps its CRDs in a subchart and creates objects of those kinds in
## the same release. helm 4 does not pick the CRDs up within that release, so on
## a cluster where they do not exist yet the first run fails and the second one
## succeeds - checked with plain "helm install" as well, and on chart versions
## 65, 75, 86 and 90; helm 3.16 installs the same chart in one go. The retry is
## a no-op once the CRDs are there, which is the case on an existing cluster.

helmfile -l name=kube-prometheus-stack sync --concurrency 1 \
    || helmfile -l name=kube-prometheus-stack sync --concurrency 1

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









