package ru.dankoy.subscriptionsholder.subscriptions_holder.config;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.observation.ClientRequestObservationContext;
import org.springframework.http.server.observation.ServerRequestObservationContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

@Configuration
public class ObservabilityConfig {

  @Bean
  ObservationRegistryCustomizer<ObservationRegistry> skipActuatorEndpointsFromObservation() {
    PathMatcher pathMatcher = new AntPathMatcher("/");
    return registry ->
        registry
            .observationConfig()
            .observationPredicate(
                (name, context) -> {
                  if (context instanceof ServerRequestObservationContext observationContext) {
                    return !pathMatcher.match(
                        "/actuator/**", observationContext.getCarrier().getRequestURI());
                  } else {
                    return true;
                  }
                });
  }

  @Bean
  ObservationRegistryCustomizer<ObservationRegistry> skipSecuritySpansFromObservation() {
    return registry ->
        registry
            .observationConfig()
            .observationPredicate((name, context) -> !name.startsWith("spring.security"));
  }

  @Bean
  ObservationRegistryCustomizer<ObservationRegistry> skipEurekaRegistrySpansFromObservation() {
    // this excludes output requests from service made by some rest client.
    return registry ->
        registry
            .observationConfig()
            .observationPredicate(
                (name, context) -> {
                  if (context instanceof ClientRequestObservationContext serverContext) {
                    return !serverContext.getCarrier().getURI().getPath().startsWith("/eureka");
                  } else {
                    return true;
                  }
                });
  }
}
