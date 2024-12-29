package ru.dankoy.subscriptions_scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration;

@SpringBootTest
@EnableAutoConfiguration(
    exclude = {EurekaClientAutoConfiguration.class, EurekaDiscoveryClientConfiguration.class})
class SubscriptionsSchedulerApplicationTests {

  @Test
  void contextLoads() {}
}
