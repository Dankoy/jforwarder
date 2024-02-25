package ru.dankoy.kafkamessageproducer.core.service.messagesender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import ru.dankoy.kafkamessageproducer.core.domain.message.CoubMessage;

@Slf4j
@RequiredArgsConstructor
public class KafkaTemplateCoubMessageImpl implements KafkaTemplateCoubMessage {

  private final KafkaTemplate<String, CoubMessage> kafkaTemplate;

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
                log.info("acknowledgement sent for {}", producerRecord.value());
              } else {
                log.error("message id:{} was not sent", producerRecord.value().getId(), ex);
              }
            });
  }
}
