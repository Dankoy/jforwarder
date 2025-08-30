#!/bin/bash

## copy secret files instead of dummy files in repo

cp .all_secrets/project/secrets/* project/secrets/
cp .all_secrets/monitoring/grafana/secrets/* monitoring/grafana/secrets/
cp .all_secrets/monitoring/mimir/mimir-secret.yaml monitoring/mimir/mimir-secret.yaml
cp .all_secrets/monitoring/minio/minio-env-secret.yaml monitoring/minio/minio-env-secret.yaml
cp .all_secrets/monitoring/alertmanager/secrets/telegram-bot-token-secret.yaml monitoring/alertmanager/secrets/telegram-bot-token-secret.yaml

