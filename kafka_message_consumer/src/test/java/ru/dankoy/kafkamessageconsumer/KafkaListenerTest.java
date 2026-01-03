package ru.dankoy.kafkamessageconsumer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CoubMessage;
import ru.dankoy.kafkamessageconsumer.core.service.consumer.MessageMaker;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka
class KafkaListenerTest implements MessageMaker {

  private static final String TOPIC = "domain-events";

  @Autowired private EmbeddedKafkaBroker embeddedKafkaBroker;

  private BlockingQueue<ConsumerRecord<String, CoubMessage>> records;

  private KafkaMessageListenerContainer<String, CoubMessage> container;

  @BeforeEach
  public void setUp() {

    Map<String, Object> configs =
        new HashMap<>(KafkaTestUtils.consumerProps(embeddedKafkaBroker, "consumer", false));
    var jsonDeserializer = new JacksonJsonDeserializer<CoubMessage>();
    jsonDeserializer.addTrustedPackages("*");
    DefaultKafkaConsumerFactory<String, CoubMessage> consumerFactory =
        new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), jsonDeserializer);
    ContainerProperties containerProperties = new ContainerProperties(TOPIC);
    container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
    records = new LinkedBlockingQueue<>();
    container.setupMessageListener((MessageListener<String, CoubMessage>) records::add);
    container.start();
    ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
  }

  @AfterEach
  void tearDown() {
    container.stop();
  }

  @Test
  void kafkaSetup_withTopic_ensureSendMessageIsReceived() throws Exception {

    // Arrange
    Map<String, Object> configs = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
    Producer<String, CoubMessage> producer =
        new DefaultKafkaProducerFactory<>(
                configs, new StringSerializer(), new JacksonJsonSerializer<CoubMessage>())
            .createProducer();

    // Act
    var message = makeMessage();
    producer.send(new ProducerRecord<>(TOPIC, "my-aggregate-id", message));
    producer.flush();

    // Assert
    ConsumerRecord<String, CoubMessage> singleRecord = records.poll(100, TimeUnit.MILLISECONDS);
    assertThat(singleRecord).isNotNull();
    assertThat(singleRecord.key()).isEqualTo("my-aggregate-id");
    assertThat(singleRecord.value()).isEqualTo(message);
  }
}
