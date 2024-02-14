package ru.dankoy.coubtagssearcher.config;

import feign.Capability;
import feign.micrometer.MicrometerCapability;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.observation.ServerRequestObservationContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

@Configuration
public class ObservabilityConfig {

    @Bean
    public Capability capability(final MeterRegistry registry) {
        return new MicrometerCapability(registry);
    }

    @Bean
    ObservationRegistryCustomizer<ObservationRegistry> skipActuatorEndpointsFromObservation() {
        PathMatcher pathMatcher = new AntPathMatcher("/");
        return (registry) ->
                registry.observationConfig()
                        .observationPredicate(
                                (name, context) -> {
                                    if (context
                                            instanceof
                                            ServerRequestObservationContext observationContext) {
                                        return !pathMatcher.match(
                                                "/actuator/**",
                                                observationContext.getCarrier().getRequestURI());
                                    } else {
                                        return true;
                                    }
                                });
    }

    @Bean
    ObservationRegistryCustomizer<ObservationRegistry> skipSecuritySpansFromObservation() {
        return (registry) ->
                registry.observationConfig()
                        .observationPredicate(
                                (name, context) -> !name.startsWith("spring.security"));
    }
}
