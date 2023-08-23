### Possible difficulties

1) Grafana's dashboards doesn't work after import json file    
   The problem is with datasource variable. Datasource has to be added in templating section of
   json.
2) Loki tempo can't get tracing data. That's why project uses loki as a log holder, and zipkin as
   tracing visualizer.
3) Somehow logback appender for loki sometimes gets incorrect traceID
4) cAdvisor can't pull from gcr.io. Permission denied