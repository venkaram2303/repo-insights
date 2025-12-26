{{/*
Expand the name of the chart.
*/}}
{{- define "repo-insights.name" -}}
{{ .Chart.Name }}
{{- end }}

{{/*
Create a default fully qualified app name.
*/}}
{{- define "repo-insights.fullname" -}}
{{- if .Values.fullnameOverride }}
{{ .Values.fullnameOverride }}
{{- else }}
{{ printf "%s-%s" .Release.Name (include "repo-insights.name" .) }}
{{- end }}
{{- end }}
