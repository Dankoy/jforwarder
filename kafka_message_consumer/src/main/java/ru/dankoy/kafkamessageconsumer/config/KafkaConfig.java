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
import ru.dankoy.kafkamessageconsumer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageconsumer.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.kafkamessageconsumer.core.service.consumer.CommunitySubscriptionMessageConsumer;
import ru.dankoy.kafkamessageconsumer.core.service.consumer.CommunitySubscriptionMessageConsumerBotSender;
import ru.dankoy.kafkamessageconsumer.core.service.consumer.TagSubscriptionMessageConsumer;
import ru.dankoy.kafkamessageconsumer.core.service.consumer.TagSubscriptionMessageConsumerBotSender;
import ru.dankoy.kafkamessageconsumer.core.service.telegrambot.TelegramBotService;


@Slf4j
@Configuration
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
    props.put(JsonDeserializer.TYPE_MAPPINGS,
        "ru.dankoy.kafkamessageproducer.core.domain.message.CommunitySubscriptionMessage:ru.dankoy.kafkamessageconsumer.core.domain.message.CommunitySubscriptionMessage"); //allow type for kafka
//    props.put(JsonDeserializer.TRUSTED_PACKAGES,
//        "ru.dankoy.kafkamessageproducer.core.domain.message"); // from producer
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 3); // max amount of messages got by one poll
    props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,
        20_000); // polling interval. how many seconds consumer can work with pack of messages before he hits new poll
    props.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, 500);
//    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 3_000);

    var kafkaProducerConsumer = new DefaultKafkaConsumerFactory<String, CommunitySubscriptionMessage>(
        props);
    kafkaProducerConsumer.setValueDeserializer(new JsonDeserializer<>(mapper));
    return kafkaProducerConsumer;
  }

  @Bean("listenerContainerFactoryCommunitySubscription")
  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, CommunitySubscriptionMessage>>
  listenerContainerFactoryCommunitySubscription(
      ConsumerFactory<String, CommunitySubscriptionMessage> consumerFactoryCommunitySubscription) {

    // this all applied only for spring boot starter

    var factory = new ConcurrentKafkaListenerContainerFactory<String, CommunitySubscriptionMessage>();
    factory.setConsumerFactory(consumerFactoryCommunitySubscription);
    factory.setBatchListener(true);
    factory.setConcurrency(
        1); // if you have one consumer but two topics or partitions, then set to two, etc.
    factory.getContainerProperties().setIdleBetweenPolls(30_000); // polling interval
    factory.getContainerProperties()
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
    props.put(JsonDeserializer.TYPE_MAPPINGS,
        "ru.dankoy.kafkamessageproducer.core.domain.message.TagSubscriptionMessage:ru.dankoy.kafkamessageconsumer.core.domain.message.TagSubscriptionMessage"); //allow type for kafka
//    props.put(JsonDeserializer.TRUSTED_PACKAGES,
//        "ru.dankoy.kafkamessageproducer.core.domain.message"); // from producer
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 3); // max amount of messages got by one poll
    props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,
        20_000); // polling interval. how many seconds consumer can work with pack of messages before he hits new poll
    props.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, 500);
//    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 3_000);

    var kafkaProducerConsumer = new DefaultKafkaConsumerFactory<String, TagSubscriptionMessage>(
        props);
    kafkaProducerConsumer.setValueDeserializer(new JsonDeserializer<>(mapper));
    return kafkaProducerConsumer;
  }

  @Bean("listenerContainerFactoryTagSubscription")
  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, TagSubscriptionMessage>>
  listenerContainerFactoryTagSubscription(
      ConsumerFactory<String, TagSubscriptionMessage> consumerFactoryCommunitySubscription) {

    // this all applied only for spring boot starter

    var factory = new ConcurrentKafkaListenerContainerFactory<String, TagSubscriptionMessage>();
    factory.setConsumerFactory(consumerFactoryCommunitySubscription);
    factory.setBatchListener(true);
    factory.setConcurrency(
        1); // if you have one consumer but two topics or partitions, then set to two, etc.
    factory.getContainerProperties().setIdleBetweenPolls(30_000); // polling interval
    factory.getContainerProperties().setPollTimeout(1_000); // wait in kafka for messages if queue is empty

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
    return TopicBuilder.name(communitySubscriptionTopic)
        .partitions(1)
        .replicas(1).build();
  }

  @Bean
  public NewTopic topicTag() {
    return TopicBuilder.name(tagSubscriptionTopic)
        .partitions(1)
        .replicas(1).build();
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
