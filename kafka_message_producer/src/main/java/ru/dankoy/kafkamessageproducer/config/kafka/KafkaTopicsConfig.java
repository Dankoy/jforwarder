package ru.dankoy.kafkamessageproducer.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {

  private final String coubCommunityTopicName;
  private final String coubTagTopicName;
  private final String coubChannelTopicName;

  public KafkaTopicsConfig(
      @Value("${application.kafka.topic.coub-com-subs}") String coubCommunityTopicName,
      @Value("${application.kafka.topic.coub-tag-subs}") String coubTagTopicName,
      @Value("${application.kafka.topic.coub-channel-subs}") String coubChannelTopicName) {
    this.coubCommunityTopicName = coubCommunityTopicName;
    this.coubTagTopicName = coubTagTopicName;
    this.coubChannelTopicName = coubChannelTopicName;
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
}
