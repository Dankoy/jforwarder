package ru.dankoy.kafkamessageproducer.core.service.messagesender.community;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import ru.dankoy.kafkamessageproducer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.message.CoubMessage;

/**
 * @deprecated in favor {@link MessageProducerServiceKafka}
 */
@Deprecated(since = "2024-02-14")
@Slf4j
@RequiredArgsConstructor
public class CommunityMessageProducerServiceKafka implements CommunityMessageProducerService {

  private final KafkaTemplate<String, CoubMessage> kafkaTemplate;

  private final String topic;

  private final Consumer<CommunitySubscriptionMessage> sendAck;

  private final Consumer<CommunitySubscriptionMessage> sendAckToRegistry;

  @Override
  public void send(CommunitySubscriptionMessage communitySubscriptionMessage) {
    try {
      log.info("message: {}", communitySubscriptionMessage);

      ProducerRecord<String, CoubMessage> producerRecord =
          new ProducerRecord<>(topic, communitySubscriptionMessage);

      producerRecord
          .headers()
          .add("subscription_type", "BY_COMMUNITY".getBytes(StandardCharsets.UTF_8));

      kafkaTemplate
          .send(producerRecord)
          .whenComplete(
              (result, ex) -> {
                if (ex == null) {
                  log.info(
                      "message id: {} was sent, offset: {}",
                      communitySubscriptionMessage.getId(),
                      result.getRecordMetadata().offset());
                  // if kafka accepted - send update last permalink in db for
                  // subscription
                  sendAck.accept(communitySubscriptionMessage);
                  // update registry
                  sendAckToRegistry.accept(communitySubscriptionMessage);
                } else {
                  log.error("message id:{} was not sent", communitySubscriptionMessage.getId(), ex);
                }
              });
    } catch (Exception ex) {
      log.error("send error, value:{}", communitySubscriptionMessage, ex);
    }
  }
}
