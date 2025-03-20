### Possible difficulties

1) Grafana's dashboards doesn't work after import json file    
   The problem is with datasource variable. Datasource has to be added in templating section of
   json.
2) Loki tempo can't get tracing data. That's why project uses loki as a log holder, and zipkin as
   tracing visualizer.
3) Somehow logback appender for loki sometimes gets incorrect traceID
4) ~~cAdvisor can't pull from gcr.io. Permission denied~~ Added cadvisor. Works great.
5) Cron in zipkin-dependencies container doesn't work. So i made cron on server for dependencies
   generation
   ```cronexp
   0 * * * * sudo docker exec --user root zipkin-dependencies /bin/sh /etc/periodic/hourly/zipkin-dependencies-today
   0 2 * * * sudo docker exec --user root zipkin-dependencies /bin/sh /etc/periodic/daily/zipkin-dependencies-yesterday
   ```
6) JMX exporter for kafka. Seems like docker container jmx exporter working as http server doesn't work with custom configs and tries to connect to localhost for some damn reason. So doesn't need to be used. Used javaagent for kafka container 
   ```docker
      - "KAFKA_JMX_OPTS=-javaagent:/prometheus/jmx_prometheus_javaagent-1.0.1.jar=12345:/prometheus/kafka-kraft-config-3_0_0.yml -Dcom.sun.management.jmxremote.rmi.port=12345 -Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.authenticate=false  -Dcom.sun.management.jmxremote.ssl=false"
   ```

   ```yml
      # for some unknown reason exporter tries to connect to localhost even if you define hostPort in config.yml.
      # broken stuff.
   jmx-exporter:
    image: 'docker.io/bitnami/jmx-exporter:1.0.1'
    container_name: jmx-exporter
    ports:
      - 5556:5556
    # host and port is necessary to pass in command because otherwise agent can't connect to localhost
    command:
      [
        "0.0.0.0:5556",
        "/jmx_exporter/config.yml"
      ]
    volumes:
      - ./kafka/kafka-kraft-config-3_0_0.yml:/jmx_exporter/config.yml
    networks:
      - jforwarder-network
   ```


7) Update loki storage schema

   evel=error ts=2024-10-13T08:20:00.176323295Z caller=main.go:70 msg="validating config" err="MULTIPLE CONFIG ERRORS FOUND, PLEASE READ CAREFULLY\n
   
   CONFIG ERROR: schema v13 is required to store Structured Metadata and use native OTLP ingestion, your schema version is v11. Set `allow_structured_metadata: false` in the `limits_config` section or set the command line argument `-validation.allow-structured-metadata=false` and restart Loki. 
   
   Then proceed to update to schema v13 or newer before re-enabling this config, search for 'Storage Schema' in the docs for the schema update procedure\n
   
   CONFIG ERROR: `tsdb` index type is required to store Structured Metadata and use native OTLP ingestion, your index type is `boltdb-shipper` (defined in the `store` parameter of the schema_config). Set `allow_structured_metadata: false` in the `limits_config` section or set the command line argument `-validation.allow-structured-metadata=false` and restart Loki. Then proceed to update the schema to use index type `tsdb` before re-enabling this config, search for 'Storage Schema' in the docs for the schema update procedure"

8) Mimir runs as one container. Should run as microservices in production. 
   To read more about storage blocks - [git](https://github.com/grafana/mimir/discussions/4187)