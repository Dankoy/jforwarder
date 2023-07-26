package ru.dankoy.kafkamessageconsumer.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import ru.dankoy.kafkamessageconsumer.core.domain.message.SubscriptionMessage;
import ru.dankoy.kafkamessageconsumer.core.service.consumer.SubscriptionMessageConsumer;
import ru.dankoy.kafkamessageconsumer.core.service.consumer.SubscriptionMessageConsumerBotSender;
import ru.dankoy.kafkamessageconsumer.core.service.telegrambot.TelegramBotService;


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
  public ConsumerFactory<String, SubscriptionMessage> consumerFactory(
      KafkaProperties kafkaProperties, ObjectMapper mapper) {
    var props = kafkaProperties.buildProducerProperties();
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    props.put(JsonDeserializer.TYPE_MAPPINGS,
        "ru.dankoy.kafkamessageproducer.core.domain.message.SubscriptionMessage:ru.dankoy.kafkamessageconsumer.core.domain.message.SubscriptionMessage"); //allow type for kafka
//    props.put(JsonDeserializer.TRUSTED_PACKAGES,
//        "ru.dankoy.kafkamessageproducer.core.domain.message"); // from producer
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 3); // max amount of messages got by one poll
    props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,
        20_000); // polling interval. how many seconds consumer can work with pack of messages before he hits new poll
    props.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, 500);
//    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 3_000);

    var kafkaProducerConsumer = new DefaultKafkaConsumerFactory<String, SubscriptionMessage>(props);
    kafkaProducerConsumer.setValueDeserializer(new JsonDeserializer<>(mapper));
    return kafkaProducerConsumer;
  }

  @Bean("listenerContainerFactory")
  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, SubscriptionMessage>>
  listenerContainerFactory(ConsumerFactory<String, SubscriptionMessage> consumerFactory) {

    // this all applied only for spring boot starter

    var factory = new ConcurrentKafkaListenerContainerFactory<String, SubscriptionMessage>();
    factory.setConsumerFactory(consumerFactory);
    factory.setBatchListener(true);
    factory.setConcurrency(
        1); // if you have one consumer but two topics or partitions, then set to two, etc.
    factory.getContainerProperties().setIdleBetweenPolls(30_000); // polling interval
    factory.getContainerProperties()
        .setPollTimeout(1_000); // wait in kafka for messages if queue is empty

    // somehow these settings make consumer consume messages every 15 seconds

    var executor = new SimpleAsyncTaskExecutor("k-consumer-");
    executor.setConcurrencyLimit(10);
    var listenerTaskExecutor = new ConcurrentTaskExecutor(executor);
    factory.getContainerProperties().setListenerTaskExecutor(listenerTaskExecutor);
    return factory;
  }

  @Bean
  public NewTopic topic() {
    return TopicBuilder.name(topicName).partitions(1).replicas(1).build();
  }

  @Bean
  public SubscriptionMessageConsumer subscriptionMessageConsumerBotSender(
      TelegramBotService telegramBotService) {
    return new SubscriptionMessageConsumerBotSender(telegramBotService);
  }

  @Bean
  public KafkaClient stringValueConsumer(
      SubscriptionMessageConsumer subscriptionMessageConsumerBotSender) {
    return new KafkaClient(subscriptionMessageConsumerBotSender);
  }

  public static class KafkaClient {

    private final SubscriptionMessageConsumer stringValueConsumer;


    public KafkaClient(SubscriptionMessageConsumer stringValueConsumer) {
      this.stringValueConsumer = stringValueConsumer;
    }

    @KafkaListener(
        topics = "${application.kafka.topic}",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "listenerContainerFactory")
    public void listen(@Payload List<SubscriptionMessage> values) {
      log.info("values, values.size:{}", values.size());
      stringValueConsumer.accept(values);
    }
  }

}
