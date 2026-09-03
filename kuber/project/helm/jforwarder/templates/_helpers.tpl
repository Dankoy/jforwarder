{{/*
Chart name, overridable with nameOverride.
*/}}
{{- define "jforwarder.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Chart label, e.g. jforwarder-0.1.0
*/}}
{{- define "jforwarder.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Labels added to every object of the release.
*/}}
{{- define "jforwarder.labels" -}}
helm.sh/chart: {{ include "jforwarder.chart" . }}
app.kubernetes.io/name: {{ include "jforwarder.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end -}}

{{/*
Labels of a single component. Call with (dict "root" $ "component" $name).
*/}}
{{- define "jforwarder.componentLabels" -}}
{{ include "jforwarder.labels" .root }}
app.kubernetes.io/component: {{ .component }}
{{- end -}}

{{/*
Image reference of a microservice: {registry}/{user}/{repository}:{tag}
Empty registry or user segments are skipped.
Call with (dict "root" $ "svc" $svc "name" $name).
*/}}
{{- define "jforwarder.image" -}}
{{- $root := .root -}}
{{- $img := .svc.image | default dict -}}
{{- $registry := default $root.Values.image.registry $img.registry -}}
{{- $user := default $root.Values.image.user $img.user -}}
{{- $repository := required (printf "services.%s.image.repository is required" .name) $img.repository -}}
{{- $tag := $img.tag | default $root.Values.image.tag | default $root.Chart.AppVersion -}}
{{- $parts := list -}}
{{- if $registry -}}{{- $parts = append $parts $registry -}}{{- end -}}
{{- if $user -}}{{- $parts = append $parts $user -}}{{- end -}}
{{- $parts = append $parts $repository -}}
{{- printf "%s:%s" (join "/" $parts) $tag -}}
{{- end -}}

{{/*
Image reference of a database. Nothing is inherited from the global image
block, so postgres is not pulled from the application registry by accident.
Call with (dict "db" $db "name" $name).
*/}}
{{- define "jforwarder.dbImage" -}}
{{- $img := .db.image | default dict -}}
{{- $repository := required (printf "databases.%s.image.repository is required" .name) $img.repository -}}
{{- $tag := required (printf "databases.%s.image.tag is required" .name) $img.tag -}}
{{- if $img.registry -}}
{{- printf "%s/%s:%s" $img.registry $repository $tag -}}
{{- else -}}
{{- printf "%s:%s" $repository $tag -}}
{{- end -}}
{{- end -}}

{{/*
Data of the <service>-config ConfigMap: JVM_PARAMS plus the extra `config`
entries. An explicit config.JVM_PARAMS wins over jvmParams.
Call with (dict "root" $ "svc" $svc).
*/}}
{{- define "jforwarder.serviceConfigData" -}}
{{- $root := .root -}}
{{- $svc := .svc -}}
{{- $data := deepCopy ($svc.config | default dict) -}}
{{- $jvm := $svc.jvmParams | default $root.Values.defaultJvmParams -}}
{{- if and $jvm (not (hasKey $data "JVM_PARAMS")) -}}
{{- $_ := set $data "JVM_PARAMS" $jvm -}}
{{- end -}}
{{- toYaml $data -}}
{{- end -}}

{{/*
Annotations that roll the pods when configs or secrets change.
Call with (dict "root" $).
*/}}
{{- define "jforwarder.configChecksums" -}}
{{- $root := .root -}}
{{- if $root.Values.restartOnConfigChange }}
checksum/config: {{ include (print $root.Template.BasePath "/configmaps.yaml") $root | sha256sum }}
checksum/secrets: {{ include (print $root.Template.BasePath "/secrets.yaml") $root | sha256sum }}
{{- end }}
{{- end -}}

{{/*
Pod scheduling settings, resolved as "per service value or global default".
Call with (dict "root" $ "svc" $svc).
*/}}
{{- define "jforwarder.podScheduling" -}}
{{- $root := .root -}}
{{- $svc := .svc -}}
{{- with $svc.nodeSelector | default $root.Values.nodeSelector }}
nodeSelector:
  {{- toYaml . | nindent 2 }}
{{- end }}
{{- with $svc.affinity | default $root.Values.affinity }}
affinity:
  {{- toYaml . | nindent 2 }}
{{- end }}
{{- with $svc.tolerations | default $root.Values.tolerations }}
tolerations:
  {{- toYaml . | nindent 2 }}
{{- end }}
{{- with $root.Values.imagePullSecrets }}
imagePullSecrets:
  {{- toYaml . | nindent 2 }}
{{- end }}
{{- with $svc.podSecurityContext | default $root.Values.podSecurityContext }}
securityContext:
  {{- toYaml . | nindent 2 }}
{{- end }}
{{- end -}}
