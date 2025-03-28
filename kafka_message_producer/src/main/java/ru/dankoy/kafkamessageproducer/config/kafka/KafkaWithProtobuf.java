package ru.dankoy.kafkamessageproducer.config.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Message;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufSerializer;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.JacksonUtils;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.generic.KafkaTemplateGeneric;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.generic.KafkaTemplateGenericImpl;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.protobuf.MessageProducerServiceKafkaProtobuf;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.protobuf.MessageProducerServiceKafkaProtobufImpl;

@Slf4j
@Configuration
public class KafkaWithProtobuf {

  private static final String HEADER_NAME = "subscription_type";

  private final String coubCommunityTopicName;
  private final String coubTagTopicName;
  private final String coubChannelTopicName;
  private final String producerClientId;
  private final String schemaRegistryUrl;

  public KafkaWithProtobuf(
      @Value("${application.kafka.topic.coub-com-subs}") String coubCommunityTopicName,
      @Value("${application.kafka.topic.coub-tag-subs}") String coubTagTopicName,
      @Value("${application.kafka.topic.coub-channel-subs}") String coubChannelTopicName,
      @Value("${application.kafka.producers.coubs.client-id}") String producerClientId,
      @Value("${application.kafka.schema-registry.url}") String schemaRegistryUrl) {
    this.coubCommunityTopicName = coubCommunityTopicName;
    this.coubTagTopicName = coubTagTopicName;
    this.coubChannelTopicName = coubChannelTopicName;
    this.producerClientId = producerClientId;
    this.schemaRegistryUrl = schemaRegistryUrl;
  }

  @Bean
  public ObjectMapper objectMapper() {
    return JacksonUtils.enhancedObjectMapper();
  }

  @Bean
  public ProducerFactory<String, Message> producerFactory(
      KafkaProperties kafkaProperties, ObjectMapper mapper, SslBundles sslBundles) {
    var props = kafkaProperties.buildProducerProperties(sslBundles);
    props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
    props.put(ProducerConfig.ACKS_CONFIG, "all");
    props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 5000);
    props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 200);
    props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 500);
    props.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, 240_000);
    props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
    props.put(ProducerConfig.CLIENT_ID_CONFIG, producerClientId);

    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaProtobufSerializer.class);
    props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);

    return new DefaultKafkaProducerFactory<>(props);
  }

  @Bean
  public KafkaTemplate<String, Message> kafkaTemplate(
      ProducerFactory<String, Message> producerFactory) {
    return new KafkaTemplate<>(producerFactory);
  }

  @Bean
  public NewTopic topicCoubCommunityMessage() {
    return TopicBuilder.name(coubCommunityTopicName).partitions(2).replicas(1).build();
  }

  @Bean
  public NewTopic topicCoubTagMessage() {
    return TopicBuilder.name(coubTagTopicName).partitions(2).replicas(1).build();
  }

  @Bean
  public NewTopic topicCoubChannelMessage() {
    return TopicBuilder.name(coubChannelTopicName).partitions(2).replicas(1).build();
  }

  @Bean
  public KafkaTemplateGeneric<String, Message> kafkaTemplateCoubMessage(
      KafkaTemplate<String, Message> kafkaTemplate) {

    return new KafkaTemplateGenericImpl<>(kafkaTemplate);
  }

  @Bean
  public MessageProducerServiceKafkaProtobuf channelMessageProducerServiceKafka(
      NewTopic topicCoubChannelMessage,
      KafkaTemplateGeneric<String, Message> kafkaTemplateCoubMessage) {
    return new MessageProducerServiceKafkaProtobufImpl(
        topicCoubChannelMessage.name(),
        kafkaTemplateCoubMessage,
        r -> r.headers().add(HEADER_NAME, "BY_CHANNEL".getBytes(StandardCharsets.UTF_8)));
  }

  @Bean
  public MessageProducerServiceKafkaProtobuf communityMessageProducerServiceKafka(
      NewTopic topicCoubCommunityMessage,
      KafkaTemplateGeneric<String, Message> kafkaTemplateCoubMessage) {
    return new MessageProducerServiceKafkaProtobufImpl(
        topicCoubCommunityMessage.name(),
        kafkaTemplateCoubMessage,
        r -> r.headers().add(HEADER_NAME, "BY_COMMUNITY".getBytes(StandardCharsets.UTF_8)));
  }

  @Bean
  public MessageProducerServiceKafkaProtobuf tagMessageProducerServiceKafka(
      NewTopic topicCoubTagMessage,
      KafkaTemplateGeneric<String, Message> kafkaTemplateCoubMessage) {
    return new MessageProducerServiceKafkaProtobufImpl(
        topicCoubTagMessage.name(),
        kafkaTemplateCoubMessage,
        r -> r.headers().add(HEADER_NAME, "BY_TAG".getBytes(StandardCharsets.UTF_8)));
  }
}
