package ru.dankoy.subscriptions_scheduler.config.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "application.scheduler")
public class SchedulerPropertiesImpl implements SchedulerProperties {

  private final String cron;
  private final int retention;
}
