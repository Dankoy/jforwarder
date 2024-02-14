package ru.dankoy.kafkamessageproducer.core.service.messagesender;

import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import ru.dankoy.kafkamessageproducer.core.domain.message.CoubMessage;

@Slf4j
@RequiredArgsConstructor
public class KafkaTemplateCoubMessageImpl implements KafkaTemplateCoubMessage {

  private final KafkaTemplate<String, CoubMessage> kafkaTemplate;

  private final Consumer<CoubMessage> sendAck;

  private final Consumer<CoubMessage> sendAckToRegistry;

  public void send(ProducerRecord<String, CoubMessage> producerRecord) {

    kafkaTemplate
        .send(producerRecord)
        .whenComplete(
            (result, ex) -> {
              if (ex == null) {
                log.info(
                    "message id: {} was sent, offset: {}",
                    producerRecord.value().getId(),
                    result.getRecordMetadata().offset());
                // if kafka accepted - send update last permalink in db for subscription
                sendAck.accept(producerRecord.value());
                // update registry
                sendAckToRegistry.accept(producerRecord.value());
                log.info("acknowledgement sent for {}", producerRecord.value());
              } else {
                log.error("message id:{} was not sent", producerRecord.value().getId(), ex);
              }
            });
  }
}
