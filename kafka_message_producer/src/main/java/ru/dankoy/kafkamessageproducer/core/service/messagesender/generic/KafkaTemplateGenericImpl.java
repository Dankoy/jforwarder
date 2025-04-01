package ru.dankoy.kafkamessageproducer.core.service.messagesender.generic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@RequiredArgsConstructor
public class KafkaTemplateGenericImpl<K, V> implements KafkaTemplateGeneric<K, V> {

  private final KafkaTemplate<K, V> kafkaTemplate;

  public void send(ProducerRecord<K, V> producerRecord) {

    kafkaTemplate
        .send(producerRecord)
        .whenComplete(
            (result, ex) -> {
              if (ex == null) {
                log.info(
                    "message id: {} was sent, offset: {}",
                    producerRecord.key(),
                    result.getRecordMetadata().offset());
                log.info("acknowledgement sent for {}", producerRecord.value());
              } else {
                log.error("message id:{} was not sent", producerRecord.key(), ex);
              }
            });
  }
}
