# Examples

## Consumer with two consumer factories and two container factories

Too much repetitive code

```java
package ru.dankoy.kafkamessageconsumer.config.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
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
import ru.dankoy.kafkamessageconsumer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageconsumer.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.kafkamessageconsumer.core.service.consumer.CommunitySubscriptionMessageConsumer;
import ru.dankoy.kafkamessageconsumer.core.service.consumer.CommunitySubscriptionMessageConsumerBotSender;
import ru.dankoy.kafkamessageconsumer.core.service.consumer.TagSubscriptionMessageConsumer;
import ru.dankoy.kafkamessageconsumer.core.service.consumer.TagSubscriptionMessageConsumerBotSender;
import ru.dankoy.kafkamessageconsumer.core.service.telegrambot.TelegramBotService;

/**
 * Works fine with different topics.
 *
 * <p>Probably can use logic with one listener container factory. Example {@link
 * KafkaBatchWithOneContainerFactoryForTwoListenersAndRecordFilterConfig}
 */
@Slf4j
// @Configuration
public class KafkaConfig {

  public final String communitySubscriptionTopic;
  public final String tagSubscriptionTopic;

  public KafkaConfig(
      @Value("${application.kafka.topic.community-subscription}") String communitySubscriptionTopic,
      @Value("${application.kafka.topic.tag-subscription}") String tagSubscriptionTopic) {
    this.communitySubscriptionTopic = communitySubscriptionTopic;
    this.tagSubscriptionTopic = tagSubscriptionTopic;
  }

  @Bean
  public ObjectMapper objectMapper() {
    return JacksonUtils.enhancedObjectMapper();
  }

  @Bean
  public ConsumerFactory<String, CommunitySubscriptionMessage> consumerFactoryCommunitySubscription(
      KafkaProperties kafkaProperties, ObjectMapper mapper) {
    var props = kafkaProperties.buildProducerProperties();
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    props.put(
        JsonDeserializer.TYPE_MAPPINGS,
        "ru.dankoy.kafkamessageproducer.core.domain.message.CommunitySubscriptionMessage:ru.dankoy.kafkamessageconsumer.core.domain.message.CommunitySubscriptionMessage"); // allow type for kafka
    //    props.put(JsonDeserializer.TRUSTED_PACKAGES,
    //        "ru.dankoy.kafkamessageproducer.core.domain.message"); // from producer
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 3); // max amount of messages got by one poll
    props.put(
        ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,
        20_000); // polling interval. how many seconds consumer can work with pack of messages
    // before he hits new poll
    props.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, 500);
    //    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 3_000);

    var kafkaProducerConsumer =
        new DefaultKafkaConsumerFactory<String, CommunitySubscriptionMessage>(props);
    kafkaProducerConsumer.setValueDeserializer(new JsonDeserializer<>(mapper));
    return kafkaProducerConsumer;
  }

  @Bean("listenerContainerFactoryCommunitySubscription")
  public KafkaListenerContainerFactory<
          ConcurrentMessageListenerContainer<String, CommunitySubscriptionMessage>>
      listenerContainerFactoryCommunitySubscription(
          ConsumerFactory<String, CommunitySubscriptionMessage>
              consumerFactoryCommunitySubscription) {

    // this all applied only for spring boot starter

    var factory =
        new ConcurrentKafkaListenerContainerFactory<String, CommunitySubscriptionMessage>();
    factory.setConsumerFactory(consumerFactoryCommunitySubscription);
    factory.setBatchListener(true);
    factory.setConcurrency(
        1); // if you have one consumer but two topics or partitions, then set to two, etc.
    factory.getContainerProperties().setIdleBetweenPolls(30_000); // polling interval
    factory
        .getContainerProperties()
        .setPollTimeout(1_000); // wait in kafka for messages if queue is empty

    // idlepeetweenpolls - in pair with maxPollInterval make time between two polls
    // somehow these settings make consumer consume messages every 15 seconds

    // filter strategy
    //    factory.setRecordFilterStrategy(
    //        record -> record.value().contains("World"));

    var executor = new SimpleAsyncTaskExecutor("k-consumer-");
    executor.setConcurrencyLimit(3);
    var listenerTaskExecutor = new ConcurrentTaskExecutor(executor);
    factory.getContainerProperties().setListenerTaskExecutor(listenerTaskExecutor);
    return factory;
  }

  @Bean
  public ConsumerFactory<String, TagSubscriptionMessage> consumerFactoryTagSubscription(
      KafkaProperties kafkaProperties, ObjectMapper mapper) {
    var props = kafkaProperties.buildProducerProperties();
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    props.put(
        JsonDeserializer.TYPE_MAPPINGS,
        "ru.dankoy.kafkamessageproducer.core.domain.message.TagSubscriptionMessage:ru.dankoy.kafkamessageconsumer.core.domain.message.TagSubscriptionMessage"); // allow type for kafka
    //    props.put(JsonDeserializer.TRUSTED_PACKAGES,
    //        "ru.dankoy.kafkamessageproducer.core.domain.message"); // from producer
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 3); // max amount of messages got by one poll
    props.put(
        ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,
        20_000); // polling interval. how many seconds consumer can work with pack of messages
    // before he hits new poll
    props.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, 500);
    //    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 3_000);

    var kafkaProducerConsumer =
        new DefaultKafkaConsumerFactory<String, TagSubscriptionMessage>(props);
    kafkaProducerConsumer.setValueDeserializer(new JsonDeserializer<>(mapper));
    return kafkaProducerConsumer;
  }

  @Bean("listenerContainerFactoryTagSubscription")
  public KafkaListenerContainerFactory<
          ConcurrentMessageListenerContainer<String, TagSubscriptionMessage>>
      listenerContainerFactoryTagSubscription(
          ConsumerFactory<String, TagSubscriptionMessage> consumerFactoryCommunitySubscription) {

    // this all applied only for spring boot starter

    var factory = new ConcurrentKafkaListenerContainerFactory<String, TagSubscriptionMessage>();
    factory.setConsumerFactory(consumerFactoryCommunitySubscription);
    factory.setBatchListener(true);
    factory.setConcurrency(
        1); // if you have one consumer but two topics or partitions, then set to two, etc.
    factory.getContainerProperties().setIdleBetweenPolls(30_000); // polling interval
    factory
        .getContainerProperties()
        .setPollTimeout(1_000); // wait in kafka for messages if queue is empty

    // idlepeetweenpolls - in pair with maxPollInterval make time between two polls
    // somehow these settings make consumer consume messages every 15 seconds

    var executor = new SimpleAsyncTaskExecutor("k-consumer-");
    executor.setConcurrencyLimit(3);
    var listenerTaskExecutor = new ConcurrentTaskExecutor(executor);
    factory.getContainerProperties().setListenerTaskExecutor(listenerTaskExecutor);
    return factory;
  }

  @Bean
  public NewTopic topicCommunity() {
    return TopicBuilder.name(communitySubscriptionTopic).partitions(1).replicas(1).build();
  }

  @Bean
  public NewTopic topicTag() {
    return TopicBuilder.name(tagSubscriptionTopic).partitions(1).replicas(1).build();
  }

  @Bean
  public CommunitySubscriptionMessageConsumer communitySubscriptionMessageConsumerBotSender(
      TelegramBotService telegramBotService) {
    return new CommunitySubscriptionMessageConsumerBotSender(telegramBotService);
  }

  @Bean
  public KafkaClientCommunitySubscription communitySubscriptionMessageConsumer(
      CommunitySubscriptionMessageConsumer communitySubscriptionMessageConsumerBotSender) {
    return new KafkaClientCommunitySubscription(communitySubscriptionMessageConsumerBotSender);
  }

  @Bean
  public TagSubscriptionMessageConsumer tagSubscriptionMessageConsumerBotSender(
      TelegramBotService telegramBotService) {
    return new TagSubscriptionMessageConsumerBotSender(telegramBotService);
  }

  @Bean
  public KafkaClientTagSubscription tagSubscriptionMessageConsumer(
      TagSubscriptionMessageConsumer tagSubscriptionMessageConsumerBotSender) {
    return new KafkaClientTagSubscription(tagSubscriptionMessageConsumerBotSender);
  }

  public static class KafkaClientCommunitySubscription {

    private final CommunitySubscriptionMessageConsumer communitySubscriptionMessageConsumer;

    public KafkaClientCommunitySubscription(
        CommunitySubscriptionMessageConsumer subscriptionMessageConsumerBotSender) {
      this.communitySubscriptionMessageConsumer = subscriptionMessageConsumerBotSender;
    }

    @KafkaListener(
        topics = "${application.kafka.topic.community-subscription}",
        groupId = "${application.kafka.consumers.community-coubs.group-id}",
        clientIdPrefix = "${application.kafka.consumers.community-coubs.client-id}",
        containerFactory = "listenerContainerFactoryCommunitySubscription")
    public void listen(@Payload List<CommunitySubscriptionMessage> values) {
      log.info("values, values.size:{}", values.size());
      communitySubscriptionMessageConsumer.accept(values);
    }
  }

  public static class KafkaClientTagSubscription {

    private final TagSubscriptionMessageConsumer tagSubscriptionMessageConsumer;

    public KafkaClientTagSubscription(
        TagSubscriptionMessageConsumer subscriptionMessageConsumerBotSender) {
      this.tagSubscriptionMessageConsumer = subscriptionMessageConsumerBotSender;
    }

    @KafkaListener(
        topics = "${application.kafka.topic.tag-subscription}",
        groupId = "${application.kafka.consumers.tag-coubs.group-id}",
        clientIdPrefix = "${application.kafka.consumers.tag-coubs.client-id}",
        containerFactory = "listenerContainerFactoryTagSubscription")
    public void listen(@Payload List<TagSubscriptionMessage> values) {
      log.info("values, values.size:{}", values.size());
      tagSubscriptionMessageConsumer.accept(values);
    }
  }
}

```

## One listener but utilizes handler methods

Cant work with batch

```java
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

```

## Batching with multiple consumer factory and multiple container factory


```java
package ru.dankoy.kafkamessageconsumer.config.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageconsumer.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.kafkamessageconsumer.core.service.consumer.CoubMessageConsumer;
import ru.dankoy.kafkamessageconsumer.core.service.consumer.CoubMessageConsumerImpl;
import ru.dankoy.kafkamessageconsumer.core.service.telegrambot.TelegramBotService;

/**
 * Works fine but multiple listener container factories are not necessary. One with filter different
 * filter strategies is enough. Look {@link
 * KafkaBatchWithOneContainerFactoryForTwoListenersAndRecordFilterConfig}
 */
@Slf4j
// @Configuration
public class KafkaBatchMultipleContainerFactoriesConfig {

  public final String coubSubscriptions;

  public KafkaBatchMultipleContainerFactoriesConfig(
      @Value("${application.kafka.topic.coub-subscriptions}") String coubSubscriptions) {
    this.coubSubscriptions = coubSubscriptions;
  }

  @Bean
  public ObjectMapper objectMapper() {
    return JacksonUtils.enhancedObjectMapper();
  }

  // Can't create this bean, because that creates circular dependency caused by injecting
  // KafkaProperties
  public Map<String, Object> kafkaCommonProperties(
      KafkaProperties kafkaProperties, SslBundles sslBundles) {

    var props = kafkaProperties.buildProducerProperties(sslBundles);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    props.put(
        JsonDeserializer.TYPE_MAPPINGS,
        "ru.dankoy.kafkamessageproducer.core.domain.message.CommunitySubscriptionMessage:"
            + "ru.dankoy.kafkamessageconsumer.core.domain.message.CommunitySubscriptionMessage, "
            + "ru.dankoy.kafkamessageproducer.core.domain.message.TagSubscriptionMessage:"
            + "ru.dankoy.kafkamessageconsumer.core.domain.message.TagSubscriptionMessage");
    // allow
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

    return props;
  }

  // This bean name should be different from consumerFactory.
  @Bean
  public ConsumerFactory<String, CommunitySubscriptionMessage> consumerFactoryCommunityMessage(
      KafkaProperties kafkaProperties, SslBundles sslBundles, ObjectMapper mapper) {

    var p = kafkaCommonProperties(kafkaProperties, sslBundles);

    var kafkaProducerConsumer =
        new DefaultKafkaConsumerFactory<String, CommunitySubscriptionMessage>(p);
    kafkaProducerConsumer.setValueDeserializer(new JsonDeserializer<>(mapper));

    return kafkaProducerConsumer;
  }

  @Bean
  public ConsumerFactory<String, TagSubscriptionMessage> consumerFactoryTagMessage(
      KafkaProperties kafkaProperties, SslBundles sslBundles, ObjectMapper mapper) {

    var p = kafkaCommonProperties(kafkaProperties, sslBundles);

    var kafkaProducerConsumer = new DefaultKafkaConsumerFactory<String, TagSubscriptionMessage>(p);
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
  RecordFilterStrategy are added in listener container factory.
  So custom factories should be applied
   */

  // The bean name should be different from kafkaListenerContainerFactory
  // Because spring then think that he tries to make circular dependency
  @Bean("jsonCommunityKafkaListenerContainerFactory")
  public KafkaListenerContainerFactory<
          ConcurrentMessageListenerContainer<String, CommunitySubscriptionMessage>>
      jsonCommunityKafkaListenerContainerFactory(
          ConsumerFactory<String, CommunitySubscriptionMessage> consumerFactoryCommunityMessage) {

    // this all applied only for spring boot starter

    var factory =
        new ConcurrentKafkaListenerContainerFactory<String, CommunitySubscriptionMessage>();
    factory.setConsumerFactory(consumerFactoryCommunityMessage);
    factory.setBatchListener(true);

    // if you have one consumer but two topics or partitions, then set to two, etc.
    factory.setConcurrency(1);
    factory.getContainerProperties().setIdleBetweenPolls(30_000); // polling interval
    factory
        .getContainerProperties()
        .setPollTimeout(1_000); // wait in kafka for messages if queue is empty

    // idlebeetweenpolls - in pair with maxPollInterval make time between two polls
    // somehow these settings make consumer consume messages every 15 seconds

    // filter strategy
    factory.setRecordFilterStrategy(
        r ->
            Arrays.equals(
                r.headers().lastHeader("subscription_type").value(), "BY_TAG".getBytes()));

    factory.getContainerProperties().setListenerTaskExecutor(concurrentTaskExecutor());
    return factory;
  }

  @Bean("jsonTagKafkaListenerContainerFactory")
  public KafkaListenerContainerFactory<
          ConcurrentMessageListenerContainer<String, TagSubscriptionMessage>>
      jsonTagKafkaListenerContainerFactory(
          ConsumerFactory<String, TagSubscriptionMessage> consumerFactoryTagMessage) {

    // this all applied only for spring boot starter

    var factory = new ConcurrentKafkaListenerContainerFactory<String, TagSubscriptionMessage>();
    factory.setConsumerFactory(consumerFactoryTagMessage);
    factory.setBatchListener(true);

    // if you have one consumer but two topics or partitions, then set to two, etc.
    factory.setConcurrency(1);
    factory.getContainerProperties().setIdleBetweenPolls(30_000); // polling interval
    factory
        .getContainerProperties()
        .setPollTimeout(1_000); // wait in kafka for messages if queue is empty

    // idlebeetweenpolls - in pair with maxPollInterval make time between two polls
    // somehow these settings make consumer consume messages every 15 seconds

    // filter strategy
    factory.setRecordFilterStrategy(
        r ->
            Arrays.equals(
                r.headers().lastHeader("subscription_type").value(), "BY_COMMUNITY".getBytes()));

    factory.getContainerProperties().setListenerTaskExecutor(concurrentTaskExecutor());
    return factory;
  }

  /*
  RecordFilterStrategy are ADDED in listener container factory.
  So custom container factories should be applied.

  By adding these filters to same listener, will not receive any messages
  filtered by these strategies.
   */

  @Bean
  public RecordFilterStrategy<String, TagSubscriptionMessage> recordFilterStrategyByCommunity() {

    return r ->
        Arrays.equals(r.headers().lastHeader("subscription_type").value(), "BY_TAG".getBytes());
  }

  @Bean
  public RecordFilterStrategy<String, CommunitySubscriptionMessage> recordFilterStrategyByTag() {

    return r ->
        Arrays.equals(
            r.headers().lastHeader("subscription_type").value(), "BY_COMMUNITY".getBytes());
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

  public static class KafkaClientSubscription {

    private final CoubMessageConsumer coubCommunityMessageConsumer;
    private final CoubMessageConsumer coubTagMessageConsumer;

    public KafkaClientSubscription(
        CoubMessageConsumer coubCommunityMessageConsumer,
        CoubMessageConsumer coubTagMessageConsumer) {
      this.coubCommunityMessageConsumer = coubCommunityMessageConsumer;
      this.coubTagMessageConsumer = coubTagMessageConsumer;
    }

    // Every listener in same group can listen its partition
    // If there are 1 partition and two listeners, then one listener won't work
    // If i want to have two listeners that reads the same topic and have filter
    // by type in them. Then these listeners have to be in different groups
    @KafkaListener(
        topics = "${application.kafka.topic.coub-subscriptions}",
        groupId = "${application.kafka.consumers.community-coubs-consumer.group-id}",
        clientIdPrefix = "${application.kafka.consumers.community-coubs-consumer.client-id}",
        containerFactory = "jsonCommunityKafkaListenerContainerFactory")
    public void listenCommunityMessages(@Payload List<CommunitySubscriptionMessage> values) {
      log.info("values, values.size:{}", values.size());
      coubCommunityMessageConsumer.accept(values);
    }

    @KafkaListener(
        topics = "${application.kafka.topic.coub-subscriptions}",
        groupId = "${application.kafka.consumers.tag-coubs-consumer.group-id}",
        clientIdPrefix = "${application.kafka.consumers.tag-coubs-consumer.client-id}",
        containerFactory = "jsonTagKafkaListenerContainerFactory")
    public void listenTagMessages(@Payload List<TagSubscriptionMessage> values) {
      log.info("values, values.size:{}", values.size());
      coubTagMessageConsumer.accept(values);
    }
  }
}

```

## One topic, one consumer factory and one container factory with record filter strategy

```yaml

application:
  kafka:
    topic:
      coub-subscriptions: "coub-subscriptions"
    consumers:
      community-coubs-consumer:
        group-id: "community-coubs-consumer-group"
        client-id: "community-coubs-consumer-id" # custom props configuration in factory
      tag-coubs-consumer:
        group-id: "tag-coubs-consumer-group"
        client-id: "tag-coubs-consumer-id" # custom props configuration in factory
      coubs-consumer: # for handlers in one listener
        group-id: "coubs-consumer-group"
        client-id: "coubs-consumer-id"

```

```java
package ru.dankoy.kafkamessageconsumer.config.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
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

/**
 * Utilizes two batch listeners, deserialization uses json jackson.
 *
 * <p>Only one listener container factory with one ConsumerFactory but for different types of
 * messages. Messages are in inheritance.
 *
 * <p>For filtering uses RecordFilterStrategy applied to same listener container factory but on
 * different listeners.
 *
 * <p>All listeners actually trying to get messages, but for one listener messages are filtered by
 * strategy, and they get 0 messages. Only correct listener gets messages to process.
 */
@Slf4j
@Configuration
public class KafkaBatchWithOneContainerFactoryForTwoListenersAndRecordFilterConfig {

  public final String coubSubscriptions;

  public KafkaBatchWithOneContainerFactoryForTwoListenersAndRecordFilterConfig(
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
    executor.setConcurrencyLimit(3);
    return new ConcurrentTaskExecutor(executor);
  }

  // Kafka can filter by object type but NOT with batch mode.

  // If you want to filter messages by type using filter strategy,
  // you should either create multiple KafkaListenerContainerFactory
  // or create filter strategy and use them in kafka listener annotation
  /*
  RecordFilterStrategy for the same container factory works fine
   */

  // The bean name should be different from kafkaListenerContainerFactory
  // Because spring then think that he tries to make circular dependency
  @Bean
  public KafkaListenerContainerFactory<?> jsonKafkaListenerContainerFactory(
      ConsumerFactory<String, CoubMessage> consumerFactoryCoubMessage) {

    // this all applied only for spring boot starter

    var factory = new ConcurrentKafkaListenerContainerFactory<String, CoubMessage>();
    factory.setConsumerFactory(consumerFactoryCoubMessage);
    factory.setBatchListener(true);

    // if you have one consumer but two topics or partitions, then set to two, etc.
    factory.setConcurrency(1);
    factory.getContainerProperties().setIdleBetweenPolls(30_000); // polling interval
    factory
        .getContainerProperties()
        .setPollTimeout(1_000); // wait in kafka for messages if queue is empty

    // idlebeetweenpolls - in pair with maxPollInterval make time between two polls
    // somehow these settings make consumer consume messages every 15 seconds

    factory.getContainerProperties().setListenerTaskExecutor(concurrentTaskExecutor());
    return factory;
  }

  /*
  RecordFilterStrategy for the same container factory works fine
   */

  @Bean
  public RecordFilterStrategy<String, TagSubscriptionMessage> recordFilterStrategyByCommunity() {

    return r ->
        Arrays.equals(r.headers().lastHeader("subscription_type").value(), "BY_TAG".getBytes());
  }

  @Bean
  public RecordFilterStrategy<String, CommunitySubscriptionMessage> recordFilterStrategyByTag() {

    return r ->
        Arrays.equals(
            r.headers().lastHeader("subscription_type").value(), "BY_COMMUNITY".getBytes());
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

  public static class KafkaClientSubscription {

    private final CoubMessageConsumer coubCommunityMessageConsumer;
    private final CoubMessageConsumer coubTagMessageConsumer;

    public KafkaClientSubscription(
        CoubMessageConsumer coubCommunityMessageConsumer,
        CoubMessageConsumer coubTagMessageConsumer) {
      this.coubCommunityMessageConsumer = coubCommunityMessageConsumer;
      this.coubTagMessageConsumer = coubTagMessageConsumer;
    }

    // Every listener in same group can listen its partition
    // If there are 1 partition and two listeners, then one listener won't work
    // If i want to have two listeners that reads the same topic and have filter
    // by type in them. Then these listeners have to be in different groups
    @KafkaListener(
        topics = "${application.kafka.topic.coub-subscriptions}",
        groupId = "${application.kafka.consumers.community-coubs-consumer.group-id}",
        clientIdPrefix = "${application.kafka.consumers.community-coubs-consumer.client-id}",
        containerFactory = "jsonKafkaListenerContainerFactory",
        filter = "recordFilterStrategyByCommunity")
    public void listenCommunityMessages(@Payload List<CommunitySubscriptionMessage> values) {
      log.info("values, values.size:{}", values.size());
      coubCommunityMessageConsumer.accept(values);
    }

    @KafkaListener(
        topics = "${application.kafka.topic.coub-subscriptions}",
        groupId = "${application.kafka.consumers.tag-coubs-consumer.group-id}",
        clientIdPrefix = "${application.kafka.consumers.tag-coubs-consumer.client-id}",
        containerFactory = "jsonKafkaListenerContainerFactory",
        filter = "recordFilterStrategyByTag")
    public void listenTagMessages(@Payload List<TagSubscriptionMessage> values) {
      log.info("values, values.size:{}", values.size());
      coubTagMessageConsumer.accept(values);
    }
  }
}

```
