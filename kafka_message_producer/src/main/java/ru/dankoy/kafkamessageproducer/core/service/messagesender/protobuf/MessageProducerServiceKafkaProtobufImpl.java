package ru.dankoy.kafkamessageproducer.core.service.messagesender.protobuf;

import com.google.protobuf.Message;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.dankoy.kafkamessageproducer.core.service.messagesender.generic.KafkaTemplateGeneric;

@Slf4j
@RequiredArgsConstructor
public class MessageProducerServiceKafkaProtobufImpl
    implements MessageProducerServiceKafkaProtobuf {

  private final String topic;

  private final KafkaTemplateGeneric<String, Message> kafkaTemplateCoubMessage;

  private final Consumer<ProducerRecord<String, Message>> recordConsumer;

  @Override
  public void send(Message message) {

    try {

      log.info("message: {}", message);

      ProducerRecord<String, Message> producerRecord = new ProducerRecord<>(topic, message);

      recordConsumer.accept(producerRecord);

      kafkaTemplateCoubMessage.send(producerRecord);

    } catch (Exception e) {
      log.error("send error, value:{}", message, e);
    }
  }
}
