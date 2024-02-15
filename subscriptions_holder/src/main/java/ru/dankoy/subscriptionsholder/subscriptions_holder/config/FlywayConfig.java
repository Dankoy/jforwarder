package ru.dankoy.subscriptionsholder.subscriptions_holder.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;

// @Configuration
public class FlywayConfig {

  //  @Bean
  public FlywayMigrationStrategy flywayMigrationStrategy() {
    return strategy -> {
      strategy.repair();
      strategy.migrate();
    };
  }
}
