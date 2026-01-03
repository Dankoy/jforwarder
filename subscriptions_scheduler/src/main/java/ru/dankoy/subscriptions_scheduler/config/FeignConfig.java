package ru.dankoy.subscriptions_scheduler.config;

import feign.Logger.Level;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

  @Bean
  Level feignLoggerLevel() {
    return Level.BASIC;
  }

  @Bean
  Encoder encoder() {
    return new SpringFormEncoder();
  }
}
