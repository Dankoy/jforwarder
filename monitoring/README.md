### Possible difficulties

1) Grafana's dashboards doesn't work after import json file    
   The problem is with datasource variable. Datasource has to be added in templating section of
   json.
2) Loki tempo can't get tracing data. That's why project uses loki as a log holder, and zipkin as
   tracing visualizer.
3) Somehow logback appender for loki sometimes gets incorrect traceID
4) cAdvisor can't pull from gcr.io. Permission denied
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