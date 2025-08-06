# Monitoring kubernetes with Prometheus and Grafana

For monitoring I used [kube-prometheus-stack](https://github.com/prometheus-community/helm-charts/tree/main/charts/kube-prometheus-stack)

```shell
helm repo add prometheus-community \
    https://prometheus-community.github.io/helm-charts

helm install kube-prometheus-stack \
    prometheus-community/kube-prometheus-stack \
    -n monitoring --create-namespace
    -f monitoring/kubestack-values.yaml
```

```shell
kube-prometheus-stack has been installed. Check its status by running:
  kubectl --namespace monitoring get pods -l "release=kube-prometheus-stack"

Get Grafana 'admin' user password by running:

  kubectl --namespace monitoring get secrets kube-prometheus-stack-grafana -o jsonpath="{.data.admin-password}" | base64 -d ; echo

Access Grafana local instance:

  export POD_NAME=$(kubectl --namespace monitoring get pod -l "app.kubernetes.io/name=grafana,app.kubernetes.io/instance=kube-prometheus-stack" -oname)
  kubectl --namespace monitoring port-forward $POD_NAME 3000

```

Prometheus with remote write should have X-Scope-OrgID header which is tenant name. It is necessary to pass per tenant configuration in mimir. 

Grafana password is passed from secret. Example [here](https://github.com/grafana/alloy-scenarios/tree/main/k8s/logs)

### Add dashboards to grafana

To add dashboards it's necessary to apply configmaps containing dashboard information

### Add password to grafana admin user

Password could be applied in first deploy with secrets and linkage to it in values in grafana section:

```yaml
  admin:
    # Name of the secret. Can be templated.
    existingSecret: "grafana-admin-secret"
    userKey: admin-user
    passwordKey: admin-password
```

## Kafka monitoring

Instructions [here](https://piotrminkowski.com/2023/11/06/apache-kafka-on-kubernetes-with-strimzi/)

PodMonitor should be applied in monitoring namespace.

## Script

Or just run script

```shell
cd monitoring
./apply-all.sh
```

## Minio 

Minio as deployment (no operator) [guide](https://www.linode.com/docs/guides/deploy-minio-on-kubernetes-using-kubespray-and-ansible/)

### Storage

Minio inside k3d can't access host path. It's necessary to create own directory and add permission to it. 

k3d cluster should have host path mounted to it like this:

`/var/volumes/minio:/data/minio`

```shell
sudo chown -R user: /var/volumes && sudo chmod u+rwx /var/volumes
```

Then persistent volume should be created pointing to /data/minio with local-storage type. If used local-path type, then volume will be created inside docker in default volumes for k3d. When k3d cluster is deleted these volumes will be deleted too. As a workaround when cluster creating one can mount host path into server k3d to `/var/volumes:/var/lib/rancher/k3s/storage`. That will make all persistent volumes creating inside k3d cluster available in host machine.

I chose first variant for storage management. Mount /var/volumes to /data/ and see all local-storage persistent volumes there. Whatever creates in /var/lib/rancher/k3s/storage is not important and could be deleted with cluster.


### Minio operator

Instructions [here](https://docs.min.io/community/minio-object-store/operations/deployments/k8s-deploy-operator-helm-on-kubernetes.html)

Minio is installed with operator. It should be installed first in separate namespace.

The minio-operator/minio-operator is a legacy chart and should not be installed under normal circumstances.

```shell
helm repo add minio-operator https://operator.min.io
helm install \
  --namespace minio-operator \
  --create-namespace \
  operator minio-operator/operator
```

### Minio itself

Instructions [here](https://docs.min.io/community/minio-object-store/operations/deployments/k8s-deploy-minio-tenant-helm-on-kubernetes.html#deploy-tenant-helm)

Local copy of values

```shell
curl -sLo values.yaml https://raw.githubusercontent.com/minio/operator/master/helm/tenant/values.yaml
```

Then minio itself could be installed with helm

```shell
helm install \
--namespace minio \
--create-namespace \
--values values.yaml \
minio minio-operator/tenant
```

#### Minio secrets

To add secrets to minio using Secret kuber config, do this:

Create file with secrets

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: minio-env-configuration
  namespace: minio
type: Opaque
data:
  config.env: {base64secret}
```

Format this in base64 format 

```text
export MINIO_ROOT_USER=user
export MINIO_ROOT_PASSWORD=password
```

In helm values in tenant section add this:

```yaml
  configuration:
    name: minio-env-configuration
  configSecret: 
    name: minio-env-configuration
    existingSecret: true
```


## Mimir

Mimir is the place that writes stuff into s3 storage (minio above). Also it is possible to configure remote write for prometheus to write into mimir.

### Configuration

No proper documentation. Work through the trail and error. 

Config should be placed in secret to not pass secrets for s3.

Guide [here](https://grafana.com/docs/helm-charts/mimir-distributed/latest/run-production-environment-with-helm/configuration-with-helm/#manage-the-configuration-externally)


```yaml
apiVersion: v1
kind: Secret
metadata:
  name: mimir-secret
  namespace: mimir
data:
  MINIO_ACCESS_KEY_ID: k
  MINIO_SECRET_ACCESS_KEY: k
  MINIO_BUCKET_NAME: l=
  MINIO_ENDPOINT: l
```

Mimir endpoint should be like this `minio.minio:443`

In values in global section add

```yaml
useExternalConfig: false
global:
  extraEnvFrom:
    - secretRef:
        name: mimir-secret
  podAnnotations:
    bucketSecretVersion: "0"
```

### Errors in configs

Ingester errors could be fixed in runtimeConfig for tenant, example [here](https://github.com/grafana/mimir/discussions/3130), also [examples](https://github.com/grafana/mimir/discussions/4519)


This [guy](https://honglab.tistory.com/302) suffered the same way as me and had same errors with kube-prometheus-stack and mimir.

## Loki

Loki is installed with helm chart. 

```shell
 helm install loki grafana/loki -f monitoring/loki/values.yaml -n monitoring
```

values.yaml contains secrets. 

!!! It can't be configured like mimir to add config into separate secret.

After installation loki is available at `http://loki-gateway.monitoring/loki/api/v1/push`


Loki minio endpoint should be like this `https://minio.minio:443`. It's different from mimir
