# For full list of configurations,
# Ref: https://github.com/hazelcast/hazelcast/blob/master/hazelcast/src/main/resources/hazelcast-full-example.yaml

hazelcast:
  cluster-name: {{ .Values.cluster.name }}
  network:
    port:
      auto-increment: false
      port: {{ .Values.service.port }}
    join:
      kubernetes:
        enabled: true
        namespace: {{ .Release.Namespace | quote }}
        service-name: {{ template "hazelcast.serviceName" . }}
    rest-api:
      enabled: true
  map:
    subscriptions:
      near-cache:
              cache-local-entries: true
    default:
      event-journal:
        enabled: true
  jet:
    enabled: true
    resource-upload-enabled: true
    # number of threads in the cooperative thread pool
    #cooperative-thread-count: 8
    # period between flow control packets in milliseconds
    #flow-control-period: 100
    # number of backup copies to configure for Hazelcast IMaps used internally in a Jet job
    backup-count: 1
    # the delay after which auto-scaled jobs will restart if a new member is added to the
    # cluster. The default is 10 seconds. Has no effect on jobs with auto scaling disabled
    scale-up-delay-millis: 10000
    # Sets whether lossless job restart is enabled for the node. With
    # lossless restart you can restart the whole cluster without losing the
    # jobs and their state. The feature is implemented on top of the Persistence
    # feature of Hazelcast which persists the data to disk.
    lossless-restart-enabled: false
    # Sets the maximum number of records that can be accumulated by any single
    # Processor instance.
    #
    # Operations like grouping, sorting or joining require certain amount of
    # records to be accumulated before they can proceed. You can set this option
    # to reduce the probability of OutOfMemoryError.
    #
    # This option applies to each Processor instance separately, hence the
    # effective limit of records accumulated by each cluster member is influenced
    # by the vertex's localParallelism and the number of jobs in the cluster.
    #
    # Currently, max-processor-accumulated-records limits:
    #    - number of items sorted by the sort operation
    #    - number of distinct keys accumulated by aggregation operations
    #    - number of entries in the hash-join lookup tables
    #    - number of entries in stateful transforms
    #    - number of distinct items in distinct operation
    # The default value is Long.MAX_VALUE.
    # Note: the limit does not apply to streaming aggregations.
    #max-processor-accumulated-records: 1000000000
    edge-defaults:
      # capacity of the concurrent SPSC queue between each two processors
      queue-size: 16
      # network packet size limit in bytes, only applies to distributed edges
      #packet-size-limit: 16384
      # receive window size multiplier, only applies to distributed edges
      #receive-window-multiplier: 3