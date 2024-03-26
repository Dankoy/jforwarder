package ru.dankoy.kafkamessageproducer.core.service.messagesender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.dankoy.kafkamessageproducer.core.domain.message.CoubMessage;

class MessageProducerServiceKafkaImplTest implements MessageMaker {

  private KafkaTemplateCoubMessage kafkaTemplateCoubMessage;

  private Consumer<ProducerRecord<String, CoubMessage>> consumer;

  private MessageProducerServiceKafkaImpl messageProducerServiceKafka;

  private static final String TOPIC = "topic";

  @BeforeEach
  void setUp() {
    consumer = Mockito.mock(Consumer.class);
    kafkaTemplateCoubMessage = Mockito.mock(KafkaTemplateCoubMessage.class);

    messageProducerServiceKafka =
        new MessageProducerServiceKafkaImpl(TOPIC, kafkaTemplateCoubMessage, consumer);
  }

  @Test
  void send() {

    var message = makeMessage();

    messageProducerServiceKafka.send(message);

    verify(kafkaTemplateCoubMessage, times(1)).send(any());
    verify(consumer, times(1)).accept(any());
  }
}
