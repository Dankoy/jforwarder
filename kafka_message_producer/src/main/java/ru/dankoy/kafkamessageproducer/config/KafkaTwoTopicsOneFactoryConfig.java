package ru.dankoy.kafkamessageproducer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.dankoy.kafkamessageproducer.core.domain.message.CoubMessage;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.KafkaTemplateCoubMessage;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.KafkaTemplateCoubMessageImpl;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.MessageProducerServiceKafka;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.MessageProducerServiceKafkaImpl;

@Slf4j
@Configuration
public class KafkaTwoTopicsOneFactoryConfig {

  private static final String HEADER_NAME = "subscription_type";

  private final String coubCommunityTopicName;
  private final String coubTagTopicName;
  private final String coubChannelTopicName;
  private final String communityProducerClientId;

  public KafkaTwoTopicsOneFactoryConfig(
      @Value("${application.kafka.topic.coub-com-subs}") String coubCommunityTopicName,
      @Value("${application.kafka.topic.coub-tag-subs}") String coubTagTopicName,
      @Value("${application.kafka.topic.coub-channel-subs}") String coubChannelTopicName,
      @Value("${application.kafka.producers.coubs.client-id}") String coubsProducerClientId) {
    this.coubCommunityTopicName = coubCommunityTopicName;
    this.coubTagTopicName = coubTagTopicName;
    this.coubChannelTopicName = coubChannelTopicName;
    this.communityProducerClientId = coubsProducerClientId;
  }

  @Bean
  public ObjectMapper objectMapper() {
    return JacksonUtils.enhancedObjectMapper();
  }

  @Bean
  public ProducerFactory<String, CoubMessage> producerFactory(
      KafkaProperties kafkaProperties, ObjectMapper mapper, SslBundles sslBundles) {
    var props = kafkaProperties.buildProducerProperties(sslBundles);
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

    var kafkaProducerFactory = new DefaultKafkaProducerFactory<String, CoubMessage>(props);
    kafkaProducerFactory.setValueSerializer(new JsonSerializer<>(mapper));
    return kafkaProducerFactory;
  }

  @Bean
  public KafkaTemplate<String, CoubMessage> kafkaTemplate(
      ProducerFactory<String, CoubMessage> producerFactory) {
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
  public KafkaTemplateCoubMessage kafkaTemplateCoubMessage(
      KafkaTemplate<String, CoubMessage> kafkaTemplate) {
    return new KafkaTemplateCoubMessageImpl(kafkaTemplate);
  }

  @Bean
  public MessageProducerServiceKafka channelMessageProducerServiceKafka(
      NewTopic topicCoubChannelMessage, KafkaTemplateCoubMessage kafkaTemplateCoubMessage) {
    return new MessageProducerServiceKafkaImpl(
        topicCoubChannelMessage.name(),
        kafkaTemplateCoubMessage,
        r -> r.headers().add(HEADER_NAME, "BY_CHANNEL".getBytes(StandardCharsets.UTF_8)));
  }

  @Bean
  public MessageProducerServiceKafka communityMessageProducerServiceKafka(
      NewTopic topicCoubCommunityMessage, KafkaTemplateCoubMessage kafkaTemplateCoubMessage) {
    return new MessageProducerServiceKafkaImpl(
        topicCoubCommunityMessage.name(),
        kafkaTemplateCoubMessage,
        r -> r.headers().add(HEADER_NAME, "BY_COMMUNITY".getBytes(StandardCharsets.UTF_8)));
  }

  @Bean
  public MessageProducerServiceKafka tagMessageProducerServiceKafka(
      NewTopic topicCoubTagMessage, KafkaTemplateCoubMessage kafkaTemplateCoubMessage) {
    return new MessageProducerServiceKafkaImpl(
        topicCoubTagMessage.name(),
        kafkaTemplateCoubMessage,
        r -> r.headers().add(HEADER_NAME, "BY_TAG".getBytes(StandardCharsets.UTF_8)));
  }

  //  @Bean
  //  public CommunityMessageProducerService communityMessageProducerService(
  //      NewTopic topicCoubCommunityMessage,
  //      KafkaTemplate<String, CoubMessage> kafkaTemplate,
  //      SubscriptionService subscriptionService,
  //      SentCoubsRegisrtyService sentCoubsRegisrtyService) {
  //    return new CommunityMessageProducerServiceKafka(
  //        kafkaTemplate,
  //        topicCoubCommunityMessage.name(),
  //        subscriptionService::updatePermalink,
  //        sentCoubsRegisrtyService::create);
  //  }
  //
  //  @Bean
  //  public TagMessageProducerService tagMessageProducerService(
  //      NewTopic topicCoubTagMessage,
  //      KafkaTemplate<String, CoubMessage> kafkaTemplate,
  //      SubscriptionService subscriptionService,
  //      SentCoubsRegisrtyService sentCoubsRegisrtyService) {
  //    return new TagMessageProducerServiceKafka(
  //        kafkaTemplate,
  //        topicCoubTagMessage.name(),
  //        subscriptionService::updatePermalink,
  //        sentCoubsRegisrtyService::create);
  //  }
  //
  //  @Bean
  //  public ChannelMessageProducerService channelMessageProducerService(
  //      NewTopic topicCoubChannelMessage,
  //      KafkaTemplate<String, CoubMessage> kafkaTemplate,
  //      SubscriptionService subscriptionService,
  //      SentCoubsRegisrtyService sentCoubsRegisrtyService) {
  //    return new ChannelMessageProducerServiceKafka(
  //        kafkaTemplate,
  //        topicCoubChannelMessage.name(),
  //        subscriptionService::updatePermalink,
  //        sentCoubsRegisrtyService::create);
  //  }
}
