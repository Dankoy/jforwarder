package ru.dankoy.kafkamessageproducer.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.dankoy.kafkamessageproducer.core.domain.message.SubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.MessageProducerService;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.MessageProducerServiceKafka;
import ru.dankoy.kafkamessageproducer.core.service.subscription.SubscriptionService;


@Slf4j
@Configuration
public class KafkaConfig {

  public final String topicName;

  public KafkaConfig(@Value("${application.kafka.topic}") String topicName) {
    this.topicName = topicName;
  }

  @Bean
  public ObjectMapper objectMapper() {
    return JacksonUtils.enhancedObjectMapper();
  }

  @Bean
  public ProducerFactory<String, SubscriptionMessage> producerFactory(
      KafkaProperties kafkaProperties, ObjectMapper mapper) {
    var props = kafkaProperties.buildProducerProperties();
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
    props.put(ProducerConfig.ACKS_CONFIG, "all");
    props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 5000);
    props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 200);
    props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 500);

    var kafkaProducerFactory = new DefaultKafkaProducerFactory<String, SubscriptionMessage>(props);
    kafkaProducerFactory.setValueSerializer(new JsonSerializer<>(mapper));
    return kafkaProducerFactory;
  }

  @Bean
  public KafkaTemplate<String, SubscriptionMessage> kafkaTemplate(
      ProducerFactory<String, SubscriptionMessage> producerFactory) {
    return new KafkaTemplate<>(producerFactory);
  }

  @Bean
  public NewTopic topic() {
    return TopicBuilder
        .name(topicName)
        .partitions(1)
        .replicas(1)
        .build();
  }

  @Bean
  public MessageProducerService messageProducerService(
      NewTopic topic,
      KafkaTemplate<String, SubscriptionMessage> kafkaTemplate,
      SubscriptionService subscriptionService) {
    return new MessageProducerServiceKafka(
        kafkaTemplate,
        topic.name(),
        subscriptionService::updatePermalink);
  }

}
