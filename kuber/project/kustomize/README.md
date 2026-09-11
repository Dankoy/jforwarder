# Kustomize deployment for jforwarder

Kustomize port of the plain manifests from [kuber/project](../). It replaces
the `sed` substitution of [kuber/release.sh](../../release.sh) with the built in
`images` transformer, and it keeps the deployed version in git.

This is the default deploy of the project: [kuber/apply-all.sh](../../apply-all.sh)
builds the secrets from env files and hands everything to the overlay below.
None of these scripts takes arguments, they read
[kuber/.env.deploy](../../.env.deploy.example).

The other two flows still work: `kuber/release.sh` + `apply-all.sh` with
`DEPLOY_MODE=plain` (sed templates) and the [helm chart](../helm).

## Layout

| Path | What it is |
| --- | --- |
| [base](./base) | every object of `kuber/project`, secrets included |
| [base/secrets](./base/secrets) | env files the `secretGenerator` reads, gitignored except the `.example` ones |
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
input of the deploy scripts. It is read as `KEY=value` and never sourced, so a
typo in it cannot move a script somewhere else:

```shell
DOCKER_HUB_USER=      # empty: images as they are, for locally built k3d ones
REGISTRY_HOST=docker.io
ENVIRONMENT=production   # which overlay is deployed
DEPLOY_MODE=kustomize    # or plain, the pre kustomize flow, production only
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
../../apply-all.sh        # the same, plus the namespace
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
in its output - and that `base/secrets` holds the real env files. A release
carries the secrets with it: they are part of the build, so nothing has to be
applied separately, and nothing changes as long as those files do not.

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

`../../apply-all.sh` is not needed for a release: it adds the namespace and
nothing else that `install` does not already send.

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

From `kuber`, which also creates the namespace:

```shell
./apply-all.sh
```

or here, without the namespace step:

```shell
./release.sh install     # kubectl apply -k
./release.sh render      # print the manifests, no cluster
./release.sh namespace   # print the namespace of ENVIRONMENT
```

`namespace` is what `apply-all.sh` reports and what `DEPLOY_MODE=plain` applies
into, so the overlays stay the single place that knows which namespace an
environment deploys into. It is handy by hand as well:

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
| first deploy, new environment | `../../apply-all.sh` | namespace, manifests, secrets |
| new version, changed manifests | `./release.sh install` | manifests only |
| changed secrets | edit `base/secrets/*.env`, then `./release.sh install` | everything, only the secrets differ |

A released version needs nothing but `release.sh install`: the namespace is
already there. Changed secrets need the same command - they are built from
`base/secrets` on every run, so there is no separate step and no way to apply
a stale copy of them.

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

The base builds them itself, with a `secretGenerator` over the env files in
[base/secrets](./base/secrets). Those files are gitignored, the `.example` ones
next to them are not, so a checkout starts with:

```shell
cd base/secrets
for f in *.example; do cp "$f" "${f%.example}"; done
$EDITOR *.env          # the real values
```

Filling them in is the only setup step. After that every deploy carries the
secrets, in the right namespace, with no separate `kubectl apply`:

```shell
./release.sh install
```

A missing env file fails the build and sends nothing to the cluster, which is
the point of this arrangement: there is no longer a placeholder file that a
machine without the real values can push over a live secret.

### The env file format

`envs` wants one `KEY=value` per line and interprets nothing else. `#` starts a
comment, but quotes are kept as part of the value and `\n` stays two
characters, so a value cannot span lines:

```shell
TELEGRAM_BOT_API_TOKEN=123456:AAH...    # right
TELEGRAM_BOT_API_TOKEN="123456:AAH..."  # wrong, the quotes end up in the token
```

Keys are kept in alphabetical order. `dotenv-linter`, which super-linter runs
over the tracked `.example` files, fails the build on anything else
(`UnorderedKey`), so a new key goes in its place rather than at the end.

A quoted multi line value is not a value at all, kustomize reads each of its
lines as another key. Nothing here needs more than a line, so every secret is a
plain `envs:`. Anything that really is multi line and really is secret goes
through `files:` instead:

```yaml
secretGenerator:
  - name: some-secret
    files:
      - SOME_KEY=secrets/some-key.pem
```

### No JASYPT

These secrets used to carry four more keys - `JASYPT_MASTER_PASSWORD`,
`TELEGRAM_CHAT_SERVICE_JASYPT_MASTER_PASSWORD` and the two three line
`*_JASYPT_PARAMS` blocks. They are gone, and nothing references them any more.

The parameters were dead on arrival here. They come from
[docker-compose.yaml](../../../docker-compose.yaml), where compose itself
interpolates `${JASYPT_MASTER_PASSWORD}` out of `.env` before the container
starts. Kubernetes has no such step, and `CMD java ${..._JASYPT_PARAMS}` in the
Dockerfile expands once, so the inner placeholder is never substituted. The
running container really had this on its command line:

```shell
$ cat /proc/1/cmdline | tr '\0' '\n' | grep jasypt
-Djasypt.encryptor.password=${JASYPT_MASTER_PASSWORD}
```

The master password was redundant twice over. The services read it themselves,
straight from the environment, in
[application.yml](../../../subscriptions_holder/src/main/resources/application.yml):

```yaml
jasypt:
  encryptor:
    password: ${JASYPT_MASTER_PASSWORD:pass}
```

and nothing ever asks the encryptor for anything. The only encrypted values in
the project are the datasource defaults of those same two services:

```yaml
url:      ${POSTGRES_CONTAINER_URL:ENC(39g3EzgM/ERP7cue...)}
username: ${POSTGRES_CONTAINER_USER:ENC(Q11d86lj5Fu0dZk9...)}
password: ${POSTGRES_CONTAINER_PASSWORD:ENC(sd1CNQox07aaV9FL...)}
```

`ENC(...)` sits after the colon - it is the *default*, used only when the
variable is absent. In the cluster all three come from the secrets and shadow
it, so jasypt-spring-boot never creates its lazy encryptor. Verified on a
cluster with both master passwords removed: `subscriptions-holder` and
`telegram-chat-service` started, served reads and writes through their REST
API, and their logs contain zero mentions of jasypt.

**This holds only while those variables are in the secrets.** Drop
`POSTGRES_CONTAINER_PASSWORD` and the `ENC(...)` default takes over, jasypt is
asked to decrypt it, and without the right master password the service fails to
start with `Failed to bind properties under 'spring.datasource.password'`. That
is what the encrypted defaults are for: running a service locally with no
environment at all. In the cluster there is always an environment.

The helm chart and `kuber/project/secrets` still carry these keys; they were
left alone with the rest of the pre kustomize flow.

### Names are not hashed

[base/kustomization.yaml](./base/kustomization.yaml) sets
`disableNameSuffixHash: true`, so the objects are named `telegram-bot-secret`
and friends, exactly as before.

The kustomize default is the opposite, and its upside is real: the name changes
with the content, so a rotated secret rolls the pods that read it instead of
leaving them on the old values until someone runs `kubectl rollout restart`.
Two things argue against it here. The database secrets are consumed by the
statefulsets of [base/statefulsets](./base/statefulsets), which have no surge -
turning the hash on renames them and takes both databases down on the next
apply. And every rotation leaves the previous secret behind, holding real
credentials, because nothing in this repo prunes (see "Deleting things").

Turning it on later is one line plus a plan for those two points.

### The old flow is still there

`kuber/project/secrets`, [kuber/secrets.sh](../../secrets.sh) and
`kuber/.all_secrets` are untouched and keep working for `DEPLOY_MODE=plain` and
for the [helm chart](../helm). They are no longer part of the kustomize deploy,
so `base/secrets/*.env` and `.all_secrets` are two separate copies of the same
credentials - whichever flow is used has to be the one that is kept current.

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
  [release.sh](./release.sh);
* a secret value — edit `base/secrets/<name>.env` and run `./release.sh
  install`; the pods keep the old value until they restart, the names are not
  hashed (see "Names are not hashed" above);
* a new secret key — add it to `base/secrets/<name>.env` and to the tracked
  `.example` next to it, so the next checkout knows the key exists;
* a new secret — add the env file and its `.example`, and a `secretGenerator`
  entry in [base/kustomization.yaml](./base/kustomization.yaml). The twin in
  `kuber/project/secrets` is only needed by `DEPLOY_MODE=plain` and the helm
  chart.

## What it does not cover

Everything outside `kuber/project`: namespaces, the storage class, kafka
(strimzi), the monitoring stack, the ServiceMonitors and headlamp. They are
installed by [kuber/setup-in-k3d.sh](../../setup-in-k3d.sh) as before.
