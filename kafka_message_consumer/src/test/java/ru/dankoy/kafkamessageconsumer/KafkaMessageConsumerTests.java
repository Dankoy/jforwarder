package ru.dankoy.kafkamessageconsumer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;
import ru.dankoy.kafkamessageconsumer.core.service.converter.MessageConverterImpl;
import ru.dankoy.kafkamessageconsumer.core.service.registry.SentCoubsRegistryServiceImpl;
import ru.dankoy.kafkamessageconsumer.core.service.subscription.SubscriptionServiceImpl;
import ru.dankoy.kafkamessageconsumer.core.service.telegrambot.TelegramBotServiceImpl;

@DisplayName("Test default context ")
@SpringBootTest
@EnableAutoConfiguration(
    exclude = {EurekaClientAutoConfiguration.class, EurekaDiscoveryClientConfiguration.class})
@EmbeddedKafka(
    partitions = 2,
    brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class KafkaMessageConsumerTests {

  static KafkaContainer kafka =
      new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0")).withKraft();

  @BeforeAll
  static void beforeAll() {
    kafka.start();
  }

  @AfterAll
  static void afterAll() {
    kafka.stop();
  }

  @Autowired private ApplicationContext context;

  @Test
  void contextLoads() {

    var messageConverter = context.getBean(MessageConverterImpl.class);
    var sentCoubsRegistryService = context.getBean(SentCoubsRegistryServiceImpl.class);
    var subscriptionService = context.getBean(SubscriptionServiceImpl.class);
    var telegramBotService = context.getBean(TelegramBotServiceImpl.class);

    assertNotNull(messageConverter);
    assertNotNull(sentCoubsRegistryService);
    assertNotNull(subscriptionService);
    assertNotNull(telegramBotService);
  }
}
