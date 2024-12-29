package ru.dankoy.subscriptions_scheduler.config.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SchedulerPropertiesImpl.class)
public class SchedulerPropertiesConfig {}
