#!/bin/bash

## installs the strimzi operator and the kafka cluster of strizmi-kafka into
## the kafka namespace

## The chart version is pinned on purpose: the manifests below are written for
## the CRD version the chart ships (kafka.strimzi.io/v1) and for the kafka
## versions its operator supports, so an unpinned "latest" silently breaks
## them - a newer chart dropped v1beta2 and kafka 4.0.0.

STRIMZI_CHART_VERSION="1.2.0"

helm repo add strimzi https://strimzi.io/charts/
helm repo update strimzi

helm upgrade --install strimzi-cluster-operator strimzi/strimzi-kafka-operator \
    --version "${STRIMZI_CHART_VERSION}" \
    -f kafka/strizmi-kafka/strizmi-values.yaml \
    -n kafka

## The Kafka and KafkaNodePool objects are instances of the CRDs the chart
## brings. Applying them before kubernetes serves those kinds fails with
## "no matches for kind", which is what happened when they were applied right
## after the install.

kubectl wait --for condition=established --timeout=120s \
    crd/kafkas.kafka.strimzi.io \
    crd/kafkanodepools.kafka.strimzi.io

## strizmi-values.yaml sits in the same folder but is a helm values file, not
## a manifest, so the folder is not applied as a whole.

for manifest in kafka/strizmi-kafka/*.yaml; do
    if [ "$(basename "${manifest}")" = "strizmi-values.yaml" ]; then
        continue
    fi

    kubectl apply -f "${manifest}" -n kafka
done
