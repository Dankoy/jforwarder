package ru.dankoy.kafkamessageproducer.core.service.messagesender;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import ru.dankoy.kafkamessageproducer.core.domain.message.TagSubscriptionMessage;

@Slf4j
@RequiredArgsConstructor
public class TagMessageProducerServiceKafka implements TagMessageProducerService {

  private final KafkaTemplate<String, TagSubscriptionMessage> kafkaTemplate;

  private final String topic;

  private final Consumer<TagSubscriptionMessage> sendAck;

  private final Consumer<TagSubscriptionMessage> sendAckToRegistry;

  @Override
  public void send(TagSubscriptionMessage tagSubscriptionMessage) {
    try {
      log.info("message: {}", tagSubscriptionMessage);

      ProducerRecord<String, TagSubscriptionMessage> producerRecord =
          new ProducerRecord<>(topic, tagSubscriptionMessage);

      producerRecord.headers().add("subscription_type", "BY_TAG".getBytes(StandardCharsets.UTF_8));

      kafkaTemplate.send(producerRecord)
          .whenComplete(
              (result, ex) -> {
                if (ex == null) {
                  log.info(
                      "message id: {} was sent, offset: {}",
                      tagSubscriptionMessage.id(),
                      result.getRecordMetadata().offset()
                  );
                  // if kafka accepted - send update last permalink in db for subscription
                  sendAck.accept(tagSubscriptionMessage);
                  // update registry
                  sendAckToRegistry.accept(tagSubscriptionMessage);
                  log.info("acknowledgement sent for {}", tagSubscriptionMessage);
                } else {
                  log.error("message id:{} was not sent",
                      tagSubscriptionMessage.coub().getPermalink(), ex);
                }
              });
    } catch (Exception ex) {
      log.error("send error, value:{}", tagSubscriptionMessage, ex);
    }
  }
}
