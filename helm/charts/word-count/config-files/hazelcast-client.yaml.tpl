# For full list of configurations
# Ref: https://github.com/hazelcast/hazelcast/blob/master/hazelcast/src/main/resources/hazelcast-client-full-example.yaml

hazelcast-client:
  # Specifies the cluster name. It's sent as part of the client authentication message to Hazelcast member(s).
  cluster-name: {{ .Values.cluster.name }}

  network:
    kubernetes:
      enabled: true
      namespace: {{ .Release.Namespace }}
      service-name: {{ template "hazelcast.serviceName" . }}