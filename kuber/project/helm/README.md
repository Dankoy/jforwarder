# Helm charts for jforwarder

Helm port of the plain manifests from [kuber/project](../). One chart,
[jforwarder](./jforwarder), renders every microservice of the application, both
postgres databases and the ingress.

## What is inside

| File | Renders |
| --- | --- |
| [templates/deployments.yaml](./jforwarder/templates/deployments.yaml) | `Deployment` per entry of `services` |
| [templates/services.yaml](./jforwarder/templates/services.yaml) | `Service` per entry of `services` |
| [templates/configmaps.yaml](./jforwarder/templates/configmaps.yaml) | `default-config` and `<service>-config` |
| [templates/secrets.yaml](./jforwarder/templates/secrets.yaml) | `Secret` per entry of `secrets.data` |
| [templates/databases.yaml](./jforwarder/templates/databases.yaml) | postgres `StatefulSet` + `Service` + `PersistentVolume` |
| [templates/servicemonitors.yaml](./jforwarder/templates/servicemonitors.yaml) | `ServiceMonitor` per service, disabled by default |
| [templates/ingress.yaml](./jforwarder/templates/ingress.yaml) | `Ingress` |

Object names are identical to the ones produced by the plain manifests, so
inter service hostnames (`spring-eureka-registry`, `spring-gateway`,
`subscriptions-holder-postgres-db`, ...) and the label `app: <name>-service` the
ServiceMonitors select on keep working. The only renamed object is the eureka
config map: `spring-eureka-config` became `spring-eureka-registry-config`.

## Install

Helm replaces `release.sh` (image substitution) and `apply-all.sh` (kubectl
apply) in one command:

```shell
helm upgrade --install jforwarder ./jforwarder \
  --namespace jforwarder --create-namespace \
  --set image.user=<docker hub user> \
  --set image.tag=1.9.6 \
  -f values-secrets.yaml
```

or with the wrapper, which keeps the two steps of the plain yaml flow —
`version` substitutes the image coordinates the way the old `release.sh` did,
`install` applies the chart the way `apply-all.sh` did:

```shell
./release.sh version -u <docker hub user> -t 1.9.6
./release.sh install -f values-secrets.yaml
```

`version` only writes `values-release.yaml` (gitignored), nothing is sent to the
cluster, so the substituted image can be reviewed before the release:

```yaml
image:
  registry: docker.io
  user: <docker hub user>
  tag: 1.9.6
```

`install` fails if that file is missing. Redeploying the same version is a bare
`./release.sh install`, a new version is a `version` call followed by `install`.

Flags: `version` takes `-u` user, `-t` tag, `-H` registry host; `install` takes
`-n` namespace, `-r` release name, `-f` extra values file (repeatable, layered
on top of `values-release.yaml`), `-d` dry run.

Render without a cluster:

```shell
helm template jforwarder ./jforwarder -n jforwarder --set image.user=<user> --set image.tag=1.9.6
```

Rollback and history come for free:

```shell
helm history jforwarder -n jforwarder
helm rollback jforwarder <revision> -n jforwarder
```

## Images

The image reference is assembled the same way `release.sh` did it:

```
{image.registry}/{image.user}/{services.<name>.image.repository}:{tag}
```

`tag` falls back to `services.<name>.image.tag`, then `image.tag`, then
`Chart.appVersion`. Empty `registry` or `user` segments are dropped, so a
local k3d image can be used with `--set image.registry="" --set image.user=""`.

`services.telegram-bot.image.repository` is `coub_forwarder_telegram_bot`, the
one image whose name differs from the service name (same special case the old
`release.sh` had).

## Secrets

`values.yaml` carries the same placeholders as `kuber/project/secrets`. Real
values never go into git:

```shell
cp values-secrets.example.yaml values-secrets.yaml   # gitignored
helm upgrade --install jforwarder ./jforwarder -n jforwarder -f values-secrets.yaml ...
```

If the secrets are managed outside of the chart (sealed-secrets, an operator, or
the existing `kuber/secrets.sh` flow), set `secrets.create=false`. The
deployments keep referencing the secrets by name, they just are not created by
the chart.

Both the config maps and the secrets are checksummed into pod annotations, so a
changed value restarts the pods. Set `restartOnConfigChange=false` to turn that
off.

## Monitoring

`serviceMonitor.enabled=false` by default because
[kuber/monitoring/servicemonitor](../../monitoring/servicemonitor) still applies
the very same objects. To let the chart own them:

```shell
helm upgrade --install jforwarder ./jforwarder -n jforwarder --set serviceMonitor.enabled=true ...
```

and drop `kubectl apply -f monitoring/servicemonitor` from
`monitoring/apply-all.sh`. They are created in the `serviceMonitor.namespace`
(`monitoring`) and scrape `serviceMonitor.targetNamespace` (`jforwarder`).

## Databases

The two postgres instances live under `databases`. `persistence.hostPath` plus
`createPersistentVolume: true` reproduce the manual `PersistentVolume` objects
of `kuber/project/storage`; on a cluster with a real dynamic provisioner set
`createPersistentVolume: false` and point `storageClassName` at it.

The generated PersistentVolumes are annotated `helm.sh/resource-policy: keep`,
so `helm uninstall` does not remove them. PVCs created from
`volumeClaimTemplates` are never deleted by kubernetes either — clean them by
hand when a database has to be recreated from scratch.

## Adding a service

Add an entry to `services` in `values.yaml`, nothing else:

```yaml
services:
  my-new-service:
    enabled: true
    image:
      repository: my_new_service
    port: 8090
    replicaCount: 1
    useDefaultConfig: true
    secrets: []
    config:
      SOME_URL: http://somewhere
    resources:
      requests: { cpu: 50m, memory: 256Mi }
      limits: { cpu: 300m, memory: 512Mi }
```

Per service overrides that are passed through as is when set: `strategy`,
`env`, `extraEnvFrom`, `livenessProbe`, `readinessProbe`, `startupProbe`,
`securityContext`, `podSecurityContext`, `nodeSelector`, `affinity`,
`tolerations`, `podLabels`, `podAnnotations`, `service.type`,
`service.nodePort`, `service.annotations`, `service.extraPorts`,
`serviceMonitor.*`. The plain manifests defined no probes, so none are set by
default; the actuator endpoints make `/actuator/health/liveness` and
`/actuator/health/readiness` the obvious values.

## Lint

```shell
helm lint ./jforwarder --set image.user=someuser
```

## What the chart does not cover

Everything outside `kuber/project`: namespaces, storage class, kafka (strimzi),
the monitoring stack and headlamp. They are installed by
`kuber/setup-in-k3d.sh` as before.
