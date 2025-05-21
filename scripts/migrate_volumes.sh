#!/bin/bash

./dvs.sh monitoring_minio_volume jforwarder_monitoring_minio_volume

./dvs.sh monitoring_mimir_volume jforwarder_monitoring_mimir_volume

./dvs.sh monitoring_loki_volume jforwarder_monitoring_loki_volume

./dvs.sh jforwarder_cassandra_volume jforwarder_monitoring_cassandra_volume

# grafana volume can't be migrated because when container runs it can't write to readonly database.
# Since all necessary data is already migrated in mimir + minio, that's not a problem.
#./dvs.sh monitoring_grafana_volume jforwarder_monitoring_grafana_volume

# Same problem with prometheus volume. Since all necessary data is stored in mimir + minio that is not a problem
#./dvs.sh monitoring_prometheus_volume jforwarder_monitoring_prometheus_volume
