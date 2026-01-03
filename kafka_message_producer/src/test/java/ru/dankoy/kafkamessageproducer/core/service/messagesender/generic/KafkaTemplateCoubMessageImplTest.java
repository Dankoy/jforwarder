package ru.dankoy.kafkamessageproducer.core.service.messagesender.generic;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;
import ru.dankoy.kafkamessageproducer.core.domain.message.CoubMessage;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.generic.KafkaTemplateCoubMessageImplTest.KafkaTestContainersConfiguration;

@SpringBootTest(classes = {KafkaTemplateGenericImpl.class})
@Import(KafkaTestContainersConfiguration.class)
@DirtiesContext
class KafkaTemplateCoubMessageImplTest implements MessageMaker {

  private static final String topic = "topic1";
  private static final String groupId = "group1";

  static KafkaContainer kafka =
      new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0")).withKraft();

  @BeforeAll
  static void beforeAll() {
    kafka.start();
  }

  @AfterAll
  static void afterAll() {
    kafka.stop();
  }

  @Autowired private KafkaConsumer<String, CoubMessage> kafkaConsumer;

  @Autowired private KafkaTemplateGenericImpl<String, CoubMessage> kafkaTemplateCoubMessage;

  @Test
  void send() {

    var message = makeMessage();
    ProducerRecord<String, CoubMessage> pr = new ProducerRecord<>(topic, message);

    kafkaTemplateCoubMessage.send(pr);

    kafkaConsumer.subscribe(Collections.singletonList(topic));

    // consumer
    await()
        .pollInterval(Duration.ofSeconds(3))
        .atMost(10, SECONDS)
        .untilAsserted(
            () -> {
              ConsumerRecords<String, CoubMessage> payload =
                  kafkaConsumer.poll(Duration.ofSeconds(5));
              assertThat(payload.isEmpty()).isFalse();
              assertThat(payload)
                  .extracting(ConsumerRecord::value)
                  .containsExactlyInAnyOrder(message);
            });

    kafkaConsumer.unsubscribe();
  }

  @TestConfiguration
  static class KafkaTestContainersConfiguration {

    @Bean
    public KafkaConsumer<String, CoubMessage> kafkaConsumer() {

      var jsonDeserializer = new JacksonJsonDeserializer<CoubMessage>(CoubMessage.class);
      jsonDeserializer.addTrustedPackages("*");

      return new KafkaConsumer<String, CoubMessage>(
          consumerConfigs(), new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
      Map<String, Object> props = new HashMap<>();
      props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
      props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
      props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
      props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
      props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);
      return props;
    }

    @Bean
    public ProducerFactory<String, CoubMessage> producerFactory() {
      Map<String, Object> configProps = new HashMap<>();
      configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
      configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
      configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
      return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, CoubMessage> kafkaTemplate() {
      return new KafkaTemplate<>(producerFactory());
    }
  }
}
