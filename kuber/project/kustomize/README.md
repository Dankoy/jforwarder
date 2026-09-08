# Kustomize deployment for jforwarder

Kustomize port of the plain manifests from [kuber/project](../). It replaces
the `sed` substitution of [kuber/release.sh](../../release.sh) with the built in
`images` transformer, and it keeps the deployed version in git.

This is the default deploy of the project: [kuber/apply-all.sh](../../apply-all.sh)
applies `project/secrets` and then hands everything else to the overlay below.
None of these scripts takes arguments, they read
[kuber/.env.deploy](../../.env.deploy.example).

The other two flows still work: `kuber/release.sh` + `apply-all.sh` with
`DEPLOY_MODE=plain` (sed templates) and the [helm chart](../helm).

## Layout

| Path | What it is |
| --- | --- |
| [base](./base) | every object of `kuber/project` except the secrets |
| [overlays/production](./overlays/production/kustomization.yaml) | production, namespace `jforwarder` |
| [overlays/dev](./overlays/dev/kustomization.yaml) | dev, namespace `jforwarder-dev` |
| [overlays/test](./overlays/test/kustomization.yaml) | test, namespace `jforwarder-test` |
| [components/dynamic-storage](./components/dynamic-storage/kustomization.yaml) | database volumes from the default provisioner |
| `overlays/release-<env>` | registry + user from `.env.deploy`, generated, gitignored |
| [release.sh](./release.sh) | `version` (bump the tag), `install` (apply), `render`, `namespace` |
| [../../.env.deploy](../../.env.deploy.example) | environment, registry host, docker hub user |

`base/configmaps`, `base/services`, `base/statefulsets`, `base/storage` and
`base/ingress` are byte identical copies of the sibling folders in
`kuber/project`, so drift is one command away:

```shell
diff -r base/configmaps ../configmaps
```

`base/deployments` is generated from
[deployments/templates](../deployments/templates) once, with
`__REGISTRY__/__USER__/__IMAGE__:__TAG__` replaced by the plain image name
(`coub_smart_searcher:latest`, `coub_forwarder_telegram_bot:latest`, ...).

## Settings

`kuber/.env.deploy`, copied from
[.env.deploy.example](../../.env.deploy.example) and gitignored, is the only
input of the deploy scripts:

```shell
DOCKER_HUB_USER=      # empty: images as they are, for locally built k3d ones
REGISTRY_HOST=docker.io
ENVIRONMENT=production   # which overlay is deployed
DEPLOY_MODE=kustomize    # or plain, the pre kustomize flow of apply-all.sh
K3D_CLUSTER=my-cluster   # setup-in-k3d.sh
```

## Environments

Every environment is an overlay of [overlays](./overlays), chosen with
`ENVIRONMENT` and defaulting to `production`:

| | production | dev | test |
| --- | --- | --- | --- |
| namespace | `jforwarder` | `jforwarder-dev` | `jforwarder-test` |
| namespace object | `kuber/namespaces` | the overlay | the overlay |
| ingress host | `spring-eureka-registry` | `spring-eureka-registry-dev` | `spring-eureka-registry-test` |
| database volumes | the hostPath `PersistentVolume`s of `base/storage` | `local-path` provisioner | `local-path` provisioner |
| version | own tag in git | own tag in git | own tag in git |

With `ENVIRONMENT=dev` in `.env.deploy`:

```shell
./release.sh version      # bump the dev version, commit it
./release.sh install      # deploy it
../../apply-all.sh        # the same, plus the secrets
```

Everything else — config maps, services, resources, replicas — is the same as
in production, so an environment differs only where it has to. The manual
`PersistentVolume`s are dropped by the
[dynamic-storage](./components/dynamic-storage/kustomization.yaml) component:
they are cluster scoped objects with fixed names and fixed host directories, so
two environments would fight over them.

Adding a `stage` environment is a copy of `overlays/dev` with the namespace,
the ingress host and the tags changed; `release.sh` and `apply-all.sh` pick it
up by the folder name as soon as `ENVIRONMENT` names it, nothing else has to be
edited.

## Releasing

The deployed version lives in git, in the overlay of the environment - the
kubernetes counterpart of `PROJECT_VERSION` in
[build.gradle](../../../build.gradle):

```yaml
images:
  - name: coub_smart_searcher
    newTag: "1.9.6-SNAPSHOT"
```

Before starting, check that `ENVIRONMENT` in `.env.deploy` names the
environment being released - every command below acts on that one and says so
in its output - and that the real secrets are already in the cluster
(`../../secrets.sh` and `kubectl apply -f ../secrets`). A release does not
touch secrets.

**1. Raise the version and publish the images.** `PROJECT_VERSION` goes up in
`build.gradle` and is committed, then a GitHub release runs
[publish.yml](../../../.github/workflows/publish.yml), which pushes
`<user>/<image>:<version>` to docker hub. Nothing kubernetes related happens
yet.

**2. Write that version into the overlay and commit it.** That commit is the
record of what runs in the cluster:

```shell
cd kuber/project/kustomize
./release.sh version
git diff overlays
git commit -am "chore: deploy 1.9.7-SNAPSHOT"
```

`version` takes the tag from `build.gradle` and rewrites only the `newTag`
entries. Editing the overlay by hand or with `kustomize edit set image
coub_smart_searcher:1.9.7-SNAPSHOT` is equally fine, the script only saves the
ten repetitions. Nothing here talks to the cluster.

**3. Look at what will change.**

```shell
./release.sh render | kubectl diff -f -
```

**4. Deploy.**

```shell
./release.sh install
```

`../../apply-all.sh` is not needed for a release: the namespace is there and
the secrets did not change, and it would apply `../secrets` again - those files
hold the dummy values until `secrets.sh` has replaced them.

**5. Watch it roll.** Only the deployments whose image changed restart,
everything else answers `unchanged`:

```shell
kubectl get pods -n "$(./release.sh namespace)" -w
```

### Rolling back

The version is a commit, so the rollback is a commit as well:

```shell
git revert <the release commit>   # or set newTag back by hand
./release.sh install
```

Do not use `release.sh version` for this: it always takes the version of
`build.gradle`, which still holds the new one. Until git catches up, a single
service can be returned to its previous pod template with
`kubectl rollout undo deployment/<name> -n <namespace>`.

### Several environments

Each overlay carries its own tag, so dev can run a version production has never
seen. Releasing the same version everywhere means repeating steps 2 to 4 with
`ENVIRONMENT` switched in `.env.deploy`; the commits of the different overlays
are independent.

The registry and the docker hub user are deliberately *not* in git, the same
way `docker-compose.yaml` keeps them in `DOCKER_HUB_USER` and `publish.yml` in
a secret. They are added at deploy time, from `.env.deploy`.

## Install

From `kuber`, which also applies the secrets:

```shell
./apply-all.sh
```

or here, without the secrets step:

```shell
./release.sh install     # kubectl apply -k
./release.sh render      # print the manifests, no cluster
./release.sh namespace   # print the namespace of ENVIRONMENT
```

`namespace` is what `apply-all.sh` uses to know where to put the secrets, so
the overlays stay the single place that knows which namespace an environment
deploys into. It is handy by hand as well:

```shell
kubectl get pods -n "$(./release.sh namespace)"
```

With a `DOCKER_HUB_USER` the script writes the gitignored
`overlays/release-<environment>` overlay, which layers `REGISTRY_HOST` and that
user on top of the overlay of `ENVIRONMENT` and is what gets applied; the tag
always comes from the tracked overlay. One generated overlay per environment,
so two deploys running at the same time cannot hand each other the wrong one.
With an empty user the environment overlay is applied directly.

### Which script when

| | command | what it sends |
| --- | --- | --- |
| first deploy, new environment | `../../apply-all.sh` | namespace, secrets, manifests |
| new version, changed manifests | `./release.sh install` | manifests only |
| changed secrets | `../../secrets.sh`, then `kubectl apply -f ../secrets` | secrets only |

A released version needs nothing but `release.sh install`: the namespace is
already there and the secrets did not change. `apply-all.sh` would also apply
`project/secrets` again, and those files hold the dummy values until
`secrets.sh` has copied the real ones over them - on a machine without
`.all_secrets` that pushes the placeholders over the real secrets in the
cluster.

`kubectl apply -k` sends all objects either way; the API server changes only
the ones that differ, so a version bump rolls the deployments and everything
else answers `unchanged`.

Plain kubectl works too, no wrapper and no kustomize binary needed:

```shell
kubectl apply -k overlays/production   # locally built images, tag from git
kubectl apply -k overlays/dev          # another environment
kubectl apply -k base                  # no environment, tag latest
kubectl kustomize overlays/production  # render only
```

## Locally built images

The base refers to `coub_smart_searcher:latest` and friends without a registry,
so images built on the machine and imported into k3d are used as is:

```shell
k3d image import coub_smart_searcher:1.9.6-SNAPSHOT -c mycluster
kubectl apply -k overlays/production
```

## Secrets

The base creates no application `Secret`. `kuber/project/secrets` holds dummy values that
[kuber/secrets.sh](../../secrets.sh) overwrites with the real ones from
`kuber/.all_secrets`, and a kustomize apply of those files would push the
dummies over the real secrets in the cluster. They keep being applied the way
they are today:

```shell
kubectl apply -f ../secrets -n jforwarder
```

The deployments reference them by name (`telegram-bot-secret`,
`subscriptions-holder-secret`, `telegram-chat-service-secret`), so the order is
secrets first, then `kubectl apply -k`.

## Deleting things

`kubectl apply -k` never deletes. It reconciles the objects it is handed and
knows nothing about what was applied before, so a service removed from git
keeps running in the cluster until it is removed by hand:

```shell
kubectl delete deployment/<name>-app service/<name> configmap/<name>-config \
    -n jforwarder
```

A whole environment goes away with its overlay, the namespace included:

```shell
kubectl delete -k overlays/dev
```

Do not run that against production: `base/storage` holds the two
`PersistentVolume`s, which are cluster scoped, and deleting them detaches the
databases. The `hostPath` data survives (the volumes are `Retain`), but the
objects and the bindings have to be recreated.

Automatic pruning is not wired in on purpose. `kubectl apply --prune -l` needs
a label on everything plus an allow-list of kinds, and silently skips the kinds
missing from it; `--prune --applyset` is the intended replacement but is still
alpha. The tool that does this properly is a GitOps controller - Flux with
`prune: true`, Argo CD with `automated.prune: true` - which keeps an inventory
of what it applied. If the deploy ever moves there, it takes this section with
it.

## Changing something

* a config or service value — edit the file in `base/<folder>` and the twin in
  `kuber/project/<folder>`, they are meant to stay identical;
* a deployment — edit `deployments/templates/<name>.template` (the sed flow) and
  `base/deployments/<name>-deployment.yaml`;
* a new service — add its manifests to both places, list the new files in
  [base/kustomization.yaml](./base/kustomization.yaml), add an entry to the
  `images` of every overlay and the image name to the `IMAGES` array of
  [release.sh](./release.sh).

## What it does not cover

Everything outside `kuber/project`: namespaces, the storage class, kafka
(strimzi), the monitoring stack, the ServiceMonitors and the dashboard. They are
installed by [kuber/setup-in-k3d.sh](../../setup-in-k3d.sh) as before.
