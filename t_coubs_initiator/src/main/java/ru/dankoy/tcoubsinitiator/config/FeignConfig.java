package ru.dankoy.tcoubsinitiator.config;

import feign.Logger.Level;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    Level feignLoggerLevel() {
        return Level.BASIC;
    }
}
