package ru.dankoy.kafkamessageconsumer.config.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException.NotFound;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;
import ru.dankoy.kafkamessageconsumer.core.domain.message.ChannelSubscriptionMessage;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CoubMessage;
import ru.dankoy.kafkamessageconsumer.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.kafkamessageconsumer.core.service.consumer.CoubMessageConsumer;
import ru.dankoy.kafkamessageconsumer.core.service.consumer.CoubMessageConsumerImpl;
import ru.dankoy.kafkamessageconsumer.core.service.registry.SentCoubsRegistryService;
import ru.dankoy.kafkamessageconsumer.core.service.subscription.SubscriptionService;
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
        "ru.dankoy.kafkamessageproducer.core.domain.message.CommunitySubscriptionMessage:ru.dankoy.kafkamessageconsumer.core.domain.message.CommunitySubscriptionMessage,"
            + " ru.dankoy.kafkamessageproducer.core.domain.message.TagSubscriptionMessage:ru.dankoy.kafkamessageconsumer.core.domain.message.TagSubscriptionMessage,"
            + " ru.dankoy.kafkamessageproducer.core.domain.message.ChannelSubscriptionMessage:"
            + "ru.dankoy.kafkamessageconsumer.core.domain.message.ChannelSubscriptionMessage"); // allow
    // type
    // for
    // kafka
    //    props.put(JsonDeserializer.TRUSTED_PACKAGES,
    //        "ru.dankoy.kafkamessageproducer.core.domain.message"); // from producer

    // max amount of messages got by one poll
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 3);

    // polling interval. how many seconds consumer can work with pack of messages
    // time to process last polled records + idle between polls must be less than
    // max.poll.interval.ms.
    props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 120_000);

    // before he hits new poll
    props.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, 500);

    var kafkaProducerConsumer = new DefaultKafkaConsumerFactory<String, CoubMessage>(props);
    kafkaProducerConsumer.setValueDeserializer(new JsonDeserializer<>(mapper));

    return kafkaProducerConsumer;
  }

  @Bean
  public ConcurrentTaskExecutor concurrentTaskExecutor() {
    var executor = new SimpleAsyncTaskExecutor("k-consumer-");
    // should be more or equals to sum of all concurrent listeners
    executor.setConcurrencyLimit(6);
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
    factory.setConcurrency(2);
    factory.getContainerProperties().setIdleBetweenPolls(10_000); // polling interval
    factory
        .getContainerProperties()
        .setPollTimeout(1_000); // wait in kafka for messages if queue is empty

    factory.getContainerProperties().setAckMode(AckMode.BATCH);
    factory.setCommonErrorHandler(errorHandler());

    // idlebeetweenpolls - in pair with maxPollInterval make time between two polls
    // somehow these settings make (idleBetweenPolls=30_000 and maxPollInterval=20_000) consumer
    // consume messages every 15 seconds

    factory.getContainerProperties().setListenerTaskExecutor(concurrentTaskExecutor());
    return factory;
  }

  @Bean
  public KafkaErrorHandler errorHandler() {
    BackOff fixedBackOff = new FixedBackOff(1000L, 5);
    KafkaErrorHandler errorHandler =
        new KafkaErrorHandler(
            (consumerRecord, e) -> {
              // logic to execute when all the retry attemps are exhausted
            },
            fixedBackOff);
    errorHandler.addRetryableExceptions(SocketTimeoutException.class);
    errorHandler.addNotRetryableExceptions(NullPointerException.class);
    errorHandler.addNotRetryableExceptions(NotFound.class);
    return errorHandler;
  }

  @Bean
  public RecordFilterStrategy<String, CoubMessage> recordFilterStrategyByObjectType() {

    return r -> Arrays.equals(r.headers().lastHeader("OBJECT_TYPE").value(), "PROTOBUF".getBytes());
  }

  @Bean
  public KafkaClientSubscription communitySubscriptionMessageConsumer(
      CoubMessageConsumer coubCommunityMessageConsumer,
      CoubMessageConsumer coubTagMessageConsumer,
      CoubMessageConsumer coubChannelMessageConsumer) {
    return new KafkaClientSubscription(
        coubCommunityMessageConsumer, coubTagMessageConsumer, coubChannelMessageConsumer);
  }

  @Bean
  public CoubMessageConsumer coubCommunityMessageConsumer(
      TelegramBotService telegramBotService,
      SentCoubsRegistryService sentCoubsRegistryService,
      SubscriptionService subscriptionService) {
    return new CoubMessageConsumerImpl(
        telegramBotService::sendCommunityMessage,
        sentCoubsRegistryService::create,
        subscriptionService::updatePermalink);
  }

  @Bean
  public CoubMessageConsumer coubTagMessageConsumer(
      TelegramBotService telegramBotService,
      SentCoubsRegistryService sentCoubsRegistryService,
      SubscriptionService subscriptionService) {
    return new CoubMessageConsumerImpl(
        telegramBotService::sendTagMessage,
        sentCoubsRegistryService::create,
        subscriptionService::updatePermalink);
  }

  @Bean
  public CoubMessageConsumer coubChannelMessageConsumer(
      TelegramBotService telegramBotService,
      SentCoubsRegistryService sentCoubsRegistryService,
      SubscriptionService subscriptionService) {
    return new CoubMessageConsumerImpl(
        telegramBotService::sendChannelMessage,
        sentCoubsRegistryService::create,
        subscriptionService::updatePermalink);
  }

  public static class KafkaClientSubscription {

    private static final String LOG_MESSAGE = "values, values.size:{}";

    private final CoubMessageConsumer coubCommunityMessageConsumer;
    private final CoubMessageConsumer coubTagMessageConsumer;
    private final CoubMessageConsumer coubChannelMessageConsumer;

    public KafkaClientSubscription(
        CoubMessageConsumer coubCommunityMessageConsumer,
        CoubMessageConsumer coubTagMessageConsumer,
        CoubMessageConsumer coubChannelMessageConsumer) {
      this.coubCommunityMessageConsumer = coubCommunityMessageConsumer;
      this.coubTagMessageConsumer = coubTagMessageConsumer;
      this.coubChannelMessageConsumer = coubChannelMessageConsumer;
    }

    // Every listener in same group can listen its partition
    // If there are 1 partition and two listeners, then one listener won't work
    // If i want to have two listeners that reads the same topic and have filter
    // by type in them. Then these listeners have to be in different groups
    @KafkaListener(
        topics = "${application.kafka.topic.coub-com-subs}",
        groupId = "${application.kafka.consumers.community-coubs-consumer.group-id}",
        clientIdPrefix = "${application.kafka.consumers.community-coubs-consumer.client-id}",
        containerFactory = "jsonKafkaListenerContainerFactory",
        filter = "recordFilterStrategyByObjectType")
    public void listenCommunityMessages(@Payload List<CommunitySubscriptionMessage> values) {
      log.info(LOG_MESSAGE, values.size());
      coubCommunityMessageConsumer.accept(values);
    }

    @KafkaListener(
        topics = "${application.kafka.topic.coub-tag-subs}",
        groupId = "${application.kafka.consumers.tag-coubs-consumer.group-id}",
        clientIdPrefix = "${application.kafka.consumers.tag-coubs-consumer.client-id}",
        containerFactory = "jsonKafkaListenerContainerFactory",
        filter = "recordFilterStrategyByObjectType")
    public void listenTagMessages(@Payload List<TagSubscriptionMessage> values) {
      log.info(LOG_MESSAGE, values.size());
      coubTagMessageConsumer.accept(values);
    }

    @KafkaListener(
        topics = "${application.kafka.topic.coub-channel-subs}",
        groupId = "${application.kafka.consumers.channel-coubs-consumer.group-id}",
        clientIdPrefix = "${application.kafka.consumers.channel-coubs-consumer.client-id}",
        containerFactory = "jsonKafkaListenerContainerFactory",
        filter = "recordFilterStrategyByObjectType")
    public void listenChannelMessages(@Payload List<ChannelSubscriptionMessage> values) {
      log.info(LOG_MESSAGE, values.size());
      coubChannelMessageConsumer.accept(values);
    }
  }
}
