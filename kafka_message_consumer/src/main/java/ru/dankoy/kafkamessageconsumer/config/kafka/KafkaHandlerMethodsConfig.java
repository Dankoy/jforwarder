package ru.dankoy.kafkamessageconsumer.config.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CoubMessage;
import ru.dankoy.kafkamessageconsumer.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.kafkamessageconsumer.core.service.consumer.CoubMessageConsumer;
import ru.dankoy.kafkamessageconsumer.core.service.consumer.CoubMessageConsumerImpl;
import ru.dankoy.kafkamessageconsumer.core.service.telegrambot.TelegramBotService;

@Slf4j
// @Configuration
public class KafkaHandlerMethodsConfig {

  public final String coubSubscriptions;

  public KafkaHandlerMethodsConfig(
      @Value("${application.kafka.topic.coub-subscriptions}") String coubSubscriptions) {
    this.coubSubscriptions = coubSubscriptions;
  }

  @Bean
  public ObjectMapper objectMapper() {
    return JacksonUtils.enhancedObjectMapper();
  }

  // This bean name should be different than consumerFactory.
  @Bean
  public ConsumerFactory<String, CoubMessage> consumerFactoryCoubMessage(
      KafkaProperties kafkaProperties, SslBundles sslBundles, ObjectMapper mapper) {

    var props = kafkaProperties.buildProducerProperties(sslBundles);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    props.put(
        JsonDeserializer.TYPE_MAPPINGS,
        "ru.dankoy.kafkamessageproducer.core.domain.message.CommunitySubscriptionMessage:"
            + "ru.dankoy.kafkamessageconsumer.core.domain.message.CommunitySubscriptionMessage, "
            + "ru.dankoy.kafkamessageproducer.core.domain.message.TagSubscriptionMessage:"
            + "ru.dankoy.kafkamessageconsumer.core.domain.message.TagSubscriptionMessage"); // allow
    // type
    // for
    // kafka
    //    props.put(JsonDeserializer.TRUSTED_PACKAGES,
    //        "ru.dankoy.kafkamessageproducer.core.domain.message"); // from producer

    // max amount of messages got by one poll
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 3);

    // polling interval. how many seconds consumer can work with pack of messages
    props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 20_000);

    // before he hits new poll
    props.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, 500);
    //    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 3_000);

    var kafkaProducerConsumer = new DefaultKafkaConsumerFactory<String, CoubMessage>(props);
    kafkaProducerConsumer.setValueDeserializer(new JsonDeserializer<>(mapper));

    return kafkaProducerConsumer;
  }

  @Bean
  public ConcurrentTaskExecutor concurrentTaskExecutor() {
    var executor = new SimpleAsyncTaskExecutor("k-consumer-");
    executor.setConcurrencyLimit(4);
    return new ConcurrentTaskExecutor(executor);
  }

  // Kafka can filter by object type but NOT with batch mode.

  // If you want to filter messages by type using filter strategy,
  // you should either create multiple KafkaListenerContainerFactory
  // or create filter strategy and use them in kafka listener annotation
  /*
  RecordFilterStrategy are replaced in listener container factory.
  So custom factories should be applied
   */

  // The bean name should be different from kafkaListenerContainerFactory
  // Because spring then think that he tries to make circular dependency
  @Bean
  public KafkaListenerContainerFactory<?> jsonKafkaListenerContainerFactory(
      ConsumerFactory<String, CoubMessage> consumerFactoryCoubMessage) {

    // this all applied only for spring boot starter

    var factory = new ConcurrentKafkaListenerContainerFactory<String, CoubMessage>();
    factory.setConsumerFactory(consumerFactoryCoubMessage);
    factory.setBatchListener(false);

    // if you have one consumer but two topics or partitions, then set to two, etc.
    factory.setConcurrency(2);
    factory.getContainerProperties().setIdleBetweenPolls(30_000); // polling interval
    factory
        .getContainerProperties()
        .setPollTimeout(1_000); // wait in kafka for messages if queue is empty

    // idlebeetweenpolls - in pair with maxPollInterval make time between two polls
    // somehow these settings make consumer consume messages every 15 seconds

    // filter strategy
    //    factory.setRecordFilterStrategy(
    //        record -> record.value().contains("World"));

    factory.getContainerProperties().setListenerTaskExecutor(concurrentTaskExecutor());
    return factory;
  }

  @Bean
  public NewTopic topic() {
    return TopicBuilder.name(coubSubscriptions).partitions(1).replicas(1).build();
  }

  @Bean
  public KafkaClientSubscription communitySubscriptionMessageConsumer(
      CoubMessageConsumer coubCommunityMessageConsumer,
      CoubMessageConsumer coubTagMessageConsumer) {
    return new KafkaClientSubscription(coubCommunityMessageConsumer, coubTagMessageConsumer);
  }

  @Bean
  public CoubMessageConsumer coubCommunityMessageConsumer(TelegramBotService telegramBotService) {
    return new CoubMessageConsumerImpl(telegramBotService::sendCommunityMessage);
  }

  @Bean
  public CoubMessageConsumer coubTagMessageConsumer(TelegramBotService telegramBotService) {
    return new CoubMessageConsumerImpl(telegramBotService::sendTagMessage);
  }

  // one listener but multiple handlers

  // multithreading is configured in container factory
  @KafkaListener(
      topics = "${application.kafka.topic.coub-subscriptions}",
      groupId = "${application.kafka.consumers.coubs-consumer.group-id}",
      clientIdPrefix = "${application.kafka.consumers.coubs-consumer.client-id}",
      containerFactory = "jsonKafkaListenerContainerFactory")
  public static class KafkaClientSubscription {

    private final CoubMessageConsumer coubCommunityMessageConsumer;
    private final CoubMessageConsumer coubTagMessageConsumer;

    public KafkaClientSubscription(
        CoubMessageConsumer coubCommunityMessageConsumer,
        CoubMessageConsumer coubTagMessageConsumer) {
      this.coubCommunityMessageConsumer = coubCommunityMessageConsumer;
      this.coubTagMessageConsumer = coubTagMessageConsumer;
    }

    @KafkaHandler
    public void listenCommunityMessages(@Payload CommunitySubscriptionMessage value) {
      coubCommunityMessageConsumer.accept(value);
    }

    @KafkaHandler
    public void listenTagMessages(@Payload TagSubscriptionMessage value) {
      coubTagMessageConsumer.accept(value);
    }
  }
}
