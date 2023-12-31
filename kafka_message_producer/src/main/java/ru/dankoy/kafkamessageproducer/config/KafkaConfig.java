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
import ru.dankoy.kafkamessageproducer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.CommunityMessageProducerService;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.CommunityMessageProducerServiceKafka;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.TagMessageProducerService;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.TagMessageProducerServiceKafka;
import ru.dankoy.kafkamessageproducer.core.service.subscription.SubscriptionService;


@Slf4j
@Configuration
public class KafkaConfig {

  private final String communitySubscriptionTopicName;
  private final String tagSubscriptionTopicName;

  private final String communityProducerClientId;
  private final String tagProducerClientId;

  public KafkaConfig(
      @Value("${application.kafka.topic.community_subscription}") String communitySubscriptionTopicName,
      @Value("${application.kafka.topic.tag_subscription}") String tagSubscriptionTopicName,
      @Value("${application.kafka.producers.community-coubs.client-id}") String communityProducerClientId,
      @Value("${application.kafka.producers.tag-coubs.client-id}") String tagProducerClientId
  ) {
    this.communitySubscriptionTopicName = communitySubscriptionTopicName;
    this.tagSubscriptionTopicName = tagSubscriptionTopicName;
    this.communityProducerClientId = communityProducerClientId;
    this.tagProducerClientId = tagProducerClientId;
  }

  @Bean
  public ObjectMapper objectMapper() {
    return JacksonUtils.enhancedObjectMapper();
  }

  @Bean
  public ProducerFactory<String, CommunitySubscriptionMessage> producerFactoryCommunity(
      KafkaProperties kafkaProperties, ObjectMapper mapper) {
    var props = kafkaProperties.buildProducerProperties();
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
    props.put(ProducerConfig.ACKS_CONFIG, "all");
    props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 5000);
    props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 200);
    props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 500);
    props.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, 240_000);
    props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
    props.put(ProducerConfig.CLIENT_ID_CONFIG, communityProducerClientId);

    var kafkaProducerFactory = new DefaultKafkaProducerFactory<String, CommunitySubscriptionMessage>(
        props);
    kafkaProducerFactory.setValueSerializer(new JsonSerializer<>(mapper));
    return kafkaProducerFactory;
  }

  @Bean
  public ProducerFactory<String, TagSubscriptionMessage> producerFactoryTag(
      KafkaProperties kafkaProperties, ObjectMapper mapper) {
    var props = kafkaProperties.buildProducerProperties();
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
    props.put(ProducerConfig.ACKS_CONFIG, "all");
    props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 3_000);
    props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 300);
    props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 500);
    props.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, 240_000);
    props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
    props.put(ProducerConfig.CLIENT_ID_CONFIG, tagProducerClientId);

    var kafkaProducerFactory = new DefaultKafkaProducerFactory<String, TagSubscriptionMessage>(
        props);
    kafkaProducerFactory.setValueSerializer(new JsonSerializer<>(mapper));
    return kafkaProducerFactory;
  }

  @Bean
  public KafkaTemplate<String, CommunitySubscriptionMessage> kafkaTemplateCommunity(
      ProducerFactory<String, CommunitySubscriptionMessage> producerFactoryCommunity) {
    return new KafkaTemplate<>(producerFactoryCommunity);
  }

  @Bean
  public KafkaTemplate<String, TagSubscriptionMessage> kafkaTemplateTag(
      ProducerFactory<String, TagSubscriptionMessage> producerFactoryTag) {
    return new KafkaTemplate<>(producerFactoryTag);
  }

  @Bean
  public NewTopic topic1() {
    return TopicBuilder
        .name(communitySubscriptionTopicName)
        .partitions(1)
        .replicas(1)
        .build();
  }

  @Bean
  public NewTopic topic2() {
    return TopicBuilder
        .name(tagSubscriptionTopicName)
        .partitions(1)
        .replicas(1)
        .build();
  }

  @Bean
  public CommunityMessageProducerService communityMessageProducerService(
      NewTopic topic1,
      KafkaTemplate<String, CommunitySubscriptionMessage> kafkaTemplateCommunity,
      SubscriptionService subscriptionService) {
    return new CommunityMessageProducerServiceKafka(
        kafkaTemplateCommunity,
        topic1.name(),
        subscriptionService::updateCommunitySubscriptionPermalink);
  }

  @Bean
  public TagMessageProducerService tagMessageProducerService(
      NewTopic topic2,
      KafkaTemplate<String, TagSubscriptionMessage> kafkaTemplateTag,
      SubscriptionService subscriptionService) {
    return new TagMessageProducerServiceKafka(
        kafkaTemplateTag,
        topic2.name(),
        subscriptionService::updateTagSubscriptionPermalink);
  }

}
