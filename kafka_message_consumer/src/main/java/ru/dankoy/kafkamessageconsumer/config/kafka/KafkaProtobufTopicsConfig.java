package ru.dankoy.kafkamessageconsumer.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaProtobufTopicsConfig {

  private final String coubCommunityTopicName;
  private final String coubTagTopicName;
  private final String coubChannelTopicName;

  public KafkaProtobufTopicsConfig(
      @Value("${application.kafka.topic.protobuf-coub-com-subs}") String coubCommunityTopicName,
      @Value("${application.kafka.topic.protobuf-coub-tag-subs}") String coubTagTopicName,
      @Value("${application.kafka.topic.protobuf-coub-channel-subs}") String coubChannelTopicName) {
    this.coubCommunityTopicName = coubCommunityTopicName;
    this.coubTagTopicName = coubTagTopicName;
    this.coubChannelTopicName = coubChannelTopicName;
  }

  @Bean
  public NewTopic topicCoubCommunityMessageProtobuf() {
    return TopicBuilder.name(coubCommunityTopicName).partitions(2).replicas(1).build();
  }

  @Bean
  public NewTopic topicCoubTagMessageProtobuf() {
    return TopicBuilder.name(coubTagTopicName).partitions(2).replicas(1).build();
  }

  @Bean
  public NewTopic topicCoubChannelMessageProtobuf() {
    return TopicBuilder.name(coubChannelTopicName).partitions(2).replicas(1).build();
  }
}
