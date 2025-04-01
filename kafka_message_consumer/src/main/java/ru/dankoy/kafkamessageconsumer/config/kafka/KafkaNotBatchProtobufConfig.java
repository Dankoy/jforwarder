package ru.dankoy.kafkamessageconsumer.config.kafka;

import com.google.protobuf.Message;
import feign.FeignException.NotFound;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializer;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializerConfig;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.errors.RecordDeserializationException;
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
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;
import ru.dankoy.kafkamessageconsumer.core.service.consumer.CoubMessageConsumer;
import ru.dankoy.kafkamessageconsumer.core.service.consumer.CoubMessageConsumerImpl;
import ru.dankoy.kafkamessageconsumer.core.service.converter.MessageConverterProtobuf;
import ru.dankoy.kafkamessageconsumer.core.service.converter.MessageConverterProtobufImpl;
import ru.dankoy.kafkamessageconsumer.core.service.registry.SentCoubsRegistryService;
import ru.dankoy.kafkamessageconsumer.core.service.subscription.SubscriptionService;
import ru.dankoy.kafkamessageconsumer.core.service.telegrambot.TelegramBotService;
import ru.dankoy.protobufschemas.protos.domain.subscription.channel.v1.ChannelSubscription;
import ru.dankoy.protobufschemas.protos.domain.subscription.community.v1.CommunitySubscription;
import ru.dankoy.protobufschemas.protos.domain.subscription.tag.v1.TagSubscription;

/**
 * Utilizes two single message listeners, deserialization uses protobuf deserializer.
 *
 * <p>Only one listener container factory with one ConsumerFactory but for different types of
 * messages. Messages are not in inheritance because protobuf doesn't support inheritance.
 *
 * <p>To "filter" messages that are not compatible with protobuf using custom error handler, that
 * ignores deserialization exception.
 *
 * <p>All listeners actually trying to get messages, but for one listener messages are filtered by
 * strategy, and they get 0 messages. Only correct listener gets messages to process.
 */
@Slf4j
@Configuration
public class KafkaNotBatchProtobufConfig {

  private final String schemaRegistryUrl;

  public KafkaNotBatchProtobufConfig(
      @Value("${application.kafka.schema-registry.url}") String schemaRegistryUrl) {
    this.schemaRegistryUrl = schemaRegistryUrl;
  }

  @Bean
  public Map<String, Object> kafkaCommonProperties(
      KafkaProperties kafkaProperties, SslBundles sslBundles) {
    var props = kafkaProperties.buildProducerProperties(sslBundles);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaProtobufDeserializer.class);
    props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);

    // props.put(KafkaProtobufDeserializerConfig.SPECIFIC_PROTOBUF_VALUE_TYPE,
    // ChannelSubscription.class.getName());
    // props.put(KafkaProtobufDeserializerConfig.SPECIFIC_PROTOBUF_VALUE_TYPE,
    // CommunitySubscription.class.getName());

    // max amount of messages got by one poll
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);

    // polling interval. how many seconds consumer can work with pack of messages
    // time to process last polled records + idle between polls must be less than
    // max.poll.interval.ms.
    props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 15_000);

    // before he hits new poll
    props.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, 500);

    return props;
  }

  // This bean name should be different than consumerFactory.
  @Bean
  public ConsumerFactory<String, Message> consumerFactoryProtobufTagMessage(
      Map<String, Object> kafkaCommonProperties) {

    kafkaCommonProperties.put(
        KafkaProtobufDeserializerConfig.SPECIFIC_PROTOBUF_VALUE_TYPE,
        TagSubscription.class.getName());

    return new DefaultKafkaConsumerFactory<>(kafkaCommonProperties);
  }

  // This bean name should be different than consumerFactory.
  @Bean
  public ConsumerFactory<String, Message> consumerFactoryProtobufCommunityMessage(
      Map<String, Object> kafkaCommonProperties) {

    kafkaCommonProperties.put(
        KafkaProtobufDeserializerConfig.SPECIFIC_PROTOBUF_VALUE_TYPE,
        CommunitySubscription.class.getName());

    return new DefaultKafkaConsumerFactory<>(kafkaCommonProperties);
  }

  // This bean name should be different than consumerFactory.
  @Bean
  public ConsumerFactory<String, Message> consumerFactoryProtobufChannelMessage(
      Map<String, Object> kafkaCommonProperties) {

    kafkaCommonProperties.put(
        KafkaProtobufDeserializerConfig.SPECIFIC_PROTOBUF_VALUE_TYPE,
        ChannelSubscription.class.getName());

    return new DefaultKafkaConsumerFactory<>(kafkaCommonProperties);
  }

  @Bean
  public ConcurrentTaskExecutor protobufConcurrentTaskExecutor() {
    var executor = new SimpleAsyncTaskExecutor("k-consumer-protobuf-");
    // should be more or equals to sum of all concurrent listeners
    executor.setConcurrencyLimit(6);
    return new ConcurrentTaskExecutor(executor);
  }

  // Kafka can filter by object type but NOT with batch mode.

  // If you want to filter messages by type using filter strategy,
  // you should either create multiple KafkaListenerContainerFactory
  // or create filter strategy and use them in kafka listener annotation
  /*
   * RecordFilterStrategy for the same container factory works fine
   */

  // The bean name should be different from kafkaListenerContainerFactory
  // Because spring then think that he tries to make circular dependency
  @Bean
  public KafkaListenerContainerFactory<?> protobufTagMessageKafkaListenerContainerFactory(
      ConsumerFactory<String, Message> consumerFactoryProtobufTagMessage) {

    // this all applied only for spring boot starter

    var factory = new ConcurrentKafkaListenerContainerFactory<String, TagSubscription>();
    factory.setConsumerFactory(consumerFactoryProtobufTagMessage);
    factory.setBatchListener(false);

    // if you have one consumer but two topics or partitions, then set to two, etc.
    factory.setConcurrency(2);
    factory.getContainerProperties().setIdleBetweenPolls(5_000); // polling interval
    factory
        .getContainerProperties()
        .setPollTimeout(1_000); // wait in kafka for messages if queue is empty

    factory.getContainerProperties().setAckMode(AckMode.MANUAL);
    factory.setCommonErrorHandler(errorHandlerProtobuf());

    // idlebeetweenpolls - in pair with maxPollInterval make time between two polls
    // somehow these settings make (idleBetweenPolls=30_000 and
    // maxPollInterval=20_000) consumer
    // consume messages every 15 seconds

    factory.getContainerProperties().setListenerTaskExecutor(protobufConcurrentTaskExecutor());
    return factory;
  }

  @Bean
  public KafkaListenerContainerFactory<?> protobufCommunityMessageKafkaListenerContainerFactory(
      ConsumerFactory<String, Message> consumerFactoryProtobufCommunityMessage) {

    // this all applied only for spring boot starter

    var factory = new ConcurrentKafkaListenerContainerFactory<String, CommunitySubscription>();
    factory.setConsumerFactory(consumerFactoryProtobufCommunityMessage);
    factory.setBatchListener(false);

    // if you have one consumer but two topics or partitions, then set to two, etc.
    factory.setConcurrency(2);
    factory.getContainerProperties().setIdleBetweenPolls(5_000); // polling interval
    factory
        .getContainerProperties()
        .setPollTimeout(1_000); // wait in kafka for messages if queue is empty

    factory.getContainerProperties().setAckMode(AckMode.MANUAL);
    factory.setCommonErrorHandler(errorHandlerProtobuf());

    // idlebeetweenpolls - in pair with maxPollInterval make time between two polls
    // somehow these settings make (idleBetweenPolls=30_000 and
    // maxPollInterval=20_000) consumer
    // consume messages every 15 seconds

    factory.getContainerProperties().setListenerTaskExecutor(protobufConcurrentTaskExecutor());
    return factory;
  }

  @Bean
  public KafkaListenerContainerFactory<?> protobufChannelMessageKafkaListenerContainerFactory(
      ConsumerFactory<String, Message> consumerFactoryProtobufChannelMessage) {

    // this all applied only for spring boot starter

    var factory = new ConcurrentKafkaListenerContainerFactory<String, ChannelSubscription>();
    factory.setConsumerFactory(consumerFactoryProtobufChannelMessage);
    factory.setBatchListener(false);

    // if you have one consumer but two topics or partitions, then set to two, etc.
    factory.setConcurrency(2);
    factory.getContainerProperties().setIdleBetweenPolls(5_000); // polling interval
    factory
        .getContainerProperties()
        .setPollTimeout(1_000); // wait in kafka for messages if queue is empty

    factory.getContainerProperties().setAckMode(AckMode.MANUAL);
    factory.setCommonErrorHandler(errorHandlerProtobuf());

    // idlebeetweenpolls - in pair with maxPollInterval make time between two polls
    // somehow these settings make (idleBetweenPolls=30_000 and
    // maxPollInterval=20_000) consumer
    // consume messages every 15 seconds

    factory.getContainerProperties().setListenerTaskExecutor(protobufConcurrentTaskExecutor());
    return factory;
  }

  @Bean
  public KafkaErrorHandler errorHandlerProtobuf() {
    BackOff fixedBackOff = new FixedBackOff(1000L, 3);
    KafkaErrorHandler errorHandler =
        new KafkaErrorHandler(
            (consumerRecord, e) -> {
              // logic to execute when all the retry attemps are exhausted
            },
            fixedBackOff);
    errorHandler.addRetryableExceptions(SocketTimeoutException.class);
    errorHandler.addNotRetryableExceptions(NullPointerException.class);
    errorHandler.addNotRetryableExceptions(NotFound.class);
    errorHandler.addNotRetryableExceptions(RecordDeserializationException.class);
    return errorHandler;
  }

  @Bean
  public KafkaErrorHandler kafkaErrorHandler() {
    return new KafkaErrorHandler();
  }

  // RecordFilterStrategy doesn't filter anything, because consumer first trying
  // to deserialize
  // record
  // Then it gets deserialization exception (if any), then commits (see
  // KafkaErrorHandler)
  @Bean
  public RecordFilterStrategy<String, Message> recordFilterStrategyByObjectTypeProtobuf() {

    return r ->
        new String(r.headers().lastHeader("OBJECT_TYPE").value(), StandardCharsets.UTF_8)
            .equals("POJO");
  }

  @Bean
  public MessageConverterProtobuf messageConverterProtobuf() {
    return new MessageConverterProtobufImpl();
  }

  @Bean
  public KafkaClientSubscription kafkaClientProtobufSubscription(
      CoubMessageConsumer protobufCommunityMessageConsumer,
      CoubMessageConsumer protobufTagMessageConsumer,
      CoubMessageConsumer protobufChannelMessageConsumer,
      MessageConverterProtobuf messageConverterProtobuf) {
    return new KafkaClientSubscription(
        protobufCommunityMessageConsumer,
        protobufTagMessageConsumer,
        protobufChannelMessageConsumer,
        messageConverterProtobuf);
  }

  @Bean
  public CoubMessageConsumer protobufCommunityMessageConsumer(
      TelegramBotService telegramBotService,
      SentCoubsRegistryService sentCoubsRegistryService,
      SubscriptionService subscriptionService) {
    return new CoubMessageConsumerImpl(
        telegramBotService::sendCommunityMessage,
        sentCoubsRegistryService::create,
        subscriptionService::updatePermalink);
  }

  @Bean
  public CoubMessageConsumer protobufTagMessageConsumer(
      TelegramBotService telegramBotService,
      SentCoubsRegistryService sentCoubsRegistryService,
      SubscriptionService subscriptionService) {
    return new CoubMessageConsumerImpl(
        telegramBotService::sendTagMessage,
        sentCoubsRegistryService::create,
        subscriptionService::updatePermalink);
  }

  @Bean
  public CoubMessageConsumer protobufChannelMessageConsumer(
      TelegramBotService telegramBotService,
      SentCoubsRegistryService sentCoubsRegistryService,
      SubscriptionService subscriptionService) {
    return new CoubMessageConsumerImpl(
        telegramBotService::sendChannelMessage,
        sentCoubsRegistryService::create,
        subscriptionService::updatePermalink);
  }

  public static class KafkaClientSubscription {

    private static final String LOG_MESSAGE = "value: {}";

    private final CoubMessageConsumer coubCommunityMessageConsumer;
    private final CoubMessageConsumer coubTagMessageConsumer;
    private final CoubMessageConsumer coubChannelMessageConsumer;
    private final MessageConverterProtobuf messageConverterProtobuf;

    public KafkaClientSubscription(
        CoubMessageConsumer coubCommunityMessageConsumer,
        CoubMessageConsumer coubTagMessageConsumer,
        CoubMessageConsumer coubChannelMessageConsumer,
        MessageConverterProtobuf messageConverterProtobuf) {
      this.coubCommunityMessageConsumer = coubCommunityMessageConsumer;
      this.coubTagMessageConsumer = coubTagMessageConsumer;
      this.coubChannelMessageConsumer = coubChannelMessageConsumer;
      this.messageConverterProtobuf = messageConverterProtobuf;
    }

    // Every listener in same group can listen its partition
    // If there are 1 partition and two listeners, then one listener won't work
    // If i want to have two listeners that reads the same topic and have filter
    // by type in them. Then these listeners have to be in different groups
    @KafkaListener(
        topics = "${application.kafka.topic.protobuf-coub-com-subs}",
        groupId = "${application.kafka.consumers.community-coubs-consumer-protobuf.group-id}",
        clientIdPrefix =
            "${application.kafka.consumers.community-coubs-consumer-protobuf.client-id}",
        containerFactory = "protobufCommunityMessageKafkaListenerContainerFactory",
        filter = "recordFilterStrategyByObjectTypeProtobuf")
    public void listenCommunityMessages(
        @Payload CommunitySubscription value, Acknowledgment acknowledgment) {
      log.info(LOG_MESSAGE, value);
      var message = messageConverterProtobuf.convert(value);
      coubCommunityMessageConsumer.accept(message);
      acknowledgment.acknowledge();
    }

    @KafkaListener(
        topics = "${application.kafka.topic.protobuf-coub-tag-subs}",
        groupId = "${application.kafka.consumers.tag-coubs-consumer-protobuf.group-id}",
        clientIdPrefix = "${application.kafka.consumers.tag-coubs-consumer-protobuf.client-id}",
        containerFactory = "protobufTagMessageKafkaListenerContainerFactory",
        filter = "recordFilterStrategyByObjectTypeProtobuf")
    public void listenTagMessages(@Payload TagSubscription value, Acknowledgment acknowledgment) {
      log.info(LOG_MESSAGE, value);
      var message = messageConverterProtobuf.convert(value);
      coubTagMessageConsumer.accept(message);
      acknowledgment.acknowledge();
    }

    @KafkaListener(
        topics = "${application.kafka.topic.protobuf-coub-channel-subs}",
        groupId = "${application.kafka.consumers.channel-coubs-consumer-protobuf.group-id}",
        clientIdPrefix = "${application.kafka.consumers.channel-coubs-consumer-protobuf.client-id}",
        containerFactory = "protobufChannelMessageKafkaListenerContainerFactory",
        filter = "recordFilterStrategyByObjectTypeProtobuf")
    public void listenChannelMessages(
        @Payload ChannelSubscription value, Acknowledgment acknowledgment) {
      log.info(LOG_MESSAGE, value);
      var message = messageConverterProtobuf.convert(value);
      coubChannelMessageConsumer.accept(message);
      acknowledgment.acknowledge();
    }
  }
}
