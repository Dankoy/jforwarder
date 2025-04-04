package ru.dankoy.kafkamessageproducer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.test.context.EmbeddedKafka;
import ru.dankoy.kafkamessageproducer.core.controller.ChannelSubscriptionController;
import ru.dankoy.kafkamessageproducer.core.controller.CommunitySubscriptionController;
import ru.dankoy.kafkamessageproducer.core.controller.TagSubscriptionController;
import ru.dankoy.kafkamessageproducer.core.service.converter.MessageConverterImpl;

@DisplayName("Test default context ")
@SpringBootTest
@EnableAutoConfiguration(
    exclude = {EurekaClientAutoConfiguration.class, EurekaDiscoveryClientConfiguration.class})
@EmbeddedKafka(
    partitions = 2,
    brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class KafkaMessageProducerTests {

  @Autowired private ApplicationContext context;

  @Test
  void contextLoads() {

    var messageConverter = context.getBean(MessageConverterImpl.class);
    var kafkaTemplateCoubMessage = context.getBean("kafkaTemplateCoubMessage");
    var messageProducerServiceKafka = context.getBean("channelMessageProducerServiceKafka");
    var communityMessageProducerServiceKafka =
        context.getBean("communityMessageProducerServiceKafka");
    var tagMessageProducerServiceKafka = context.getBean("tagMessageProducerServiceKafka");
    var channelSubscriptionController = context.getBean(ChannelSubscriptionController.class);
    var communitySubscriptionController = context.getBean(CommunitySubscriptionController.class);
    var tagSubscriptionController = context.getBean(TagSubscriptionController.class);

    assertNotNull(messageConverter);
    assertNotNull(kafkaTemplateCoubMessage);
    assertNotNull(messageProducerServiceKafka);
    assertNotNull(communityMessageProducerServiceKafka);
    assertNotNull(tagMessageProducerServiceKafka);
    assertNotNull(channelSubscriptionController);
    assertNotNull(communitySubscriptionController);
    assertNotNull(tagSubscriptionController);
  }
}
