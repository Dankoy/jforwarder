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
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.generic.KafkaTemplateGeneric;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.generic.KafkaTemplateGenericImpl;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.protobuf.MessageProducerServiceKafkaProtobuf;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.protobuf.MessageProducerServiceKafkaProtobufImpl;

@Slf4j
@Configuration
public class KafkaWithProtobuf {

  private static final String HEADER_NAME = "subscription_type";
  private static final String HEADER_NAME_TYPE = "OBJECT_TYPE";

  private final String producerClientId;
  private final String schemaRegistryUrl;

  public KafkaWithProtobuf(
      @Value("${application.kafka.producers.coubs.client-id}") String producerClientId,
      @Value("${application.kafka.schema-registry.url}") String schemaRegistryUrl) {
    this.producerClientId = producerClientId;
    this.schemaRegistryUrl = schemaRegistryUrl;
  }

  @Bean
  public ProducerFactory<String, Message> protobufProducerFactory(
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
  public KafkaTemplate<String, Message> kafkaTemplateProtobufProducer(
      ProducerFactory<String, Message> protobufProducerFactory) {
    return new KafkaTemplate<>(protobufProducerFactory);
  }

  @Bean
  public KafkaTemplateGeneric<String, Message> kafkaTemplateProtobuf(
      KafkaTemplate<String, Message> kafkaTemplateProtobufProducer) {

    return new KafkaTemplateGenericImpl<>(kafkaTemplateProtobufProducer);
  }

  @Bean
  public MessageProducerServiceKafkaProtobuf channelMessageProducerServiceKafkaProtobuf(
      NewTopic topicCoubChannelMessage,
      KafkaTemplateGeneric<String, Message> kafkaTemplateProtobuf) {
    return new MessageProducerServiceKafkaProtobufImpl(
        topicCoubChannelMessage.name(),
        kafkaTemplateProtobuf,
        r ->
            r.headers()
                .add(HEADER_NAME, "BY_CHANNEL".getBytes(StandardCharsets.UTF_8))
                .add(HEADER_NAME_TYPE, "PROTOBUF".getBytes(StandardCharsets.UTF_8)));
  }

  @Bean
  public MessageProducerServiceKafkaProtobuf communityMessageProducerServiceKafkaProtobuf(
      NewTopic topicCoubCommunityMessage,
      KafkaTemplateGeneric<String, Message> kafkaTemplateProtobuf) {
    return new MessageProducerServiceKafkaProtobufImpl(
        topicCoubCommunityMessage.name(),
        kafkaTemplateProtobuf,
        r ->
            r.headers()
                .add(HEADER_NAME, "BY_COMMUNITY".getBytes(StandardCharsets.UTF_8))
                .add(HEADER_NAME_TYPE, "PROTOBUF".getBytes(StandardCharsets.UTF_8)));
  }

  @Bean
  public MessageProducerServiceKafkaProtobuf tagMessageProducerServiceKafkaProtobuf(
      NewTopic topicCoubTagMessage, KafkaTemplateGeneric<String, Message> kafkaTemplateProtobuf) {
    return new MessageProducerServiceKafkaProtobufImpl(
        topicCoubTagMessage.name(),
        kafkaTemplateProtobuf,
        r ->
            r.headers()
                .add(HEADER_NAME, "BY_TAG".getBytes(StandardCharsets.UTF_8))
                .add(HEADER_NAME_TYPE, "PROTOBUF".getBytes(StandardCharsets.UTF_8)));
  }
}
