package ru.dankoy.kafkamessageproducer.core.service.messagesender;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import ru.dankoy.kafkamessageproducer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.message.TagSubscriptionMessage;

@Slf4j
@RequiredArgsConstructor
public class CommunityMessageProducerServiceKafka implements CommunityMessageProducerService {

  private final KafkaTemplate<String, CommunitySubscriptionMessage> kafkaTemplate;

  private final String topic;

  private final Consumer<CommunitySubscriptionMessage> sendAck;

  @Override
  public void send(CommunitySubscriptionMessage communitySubscriptionMessage) {
    try {
      log.info("message: {}", communitySubscriptionMessage);

      ProducerRecord<String, CommunitySubscriptionMessage> producerRecord =
          new ProducerRecord<>(topic, communitySubscriptionMessage);

      producerRecord.headers()
          .add("subscription_type", "BY_COMMUNITY".getBytes(StandardCharsets.UTF_8));

      kafkaTemplate.send(producerRecord)
          .whenComplete(
              (result, ex) -> {
                if (ex == null) {
                  log.info(
                      "message id: {} was sent, offset: {}",
                      communitySubscriptionMessage.id(),
                      result.getRecordMetadata().offset()
                  );
                  // if kafka accepted - send update last permalink in db for subscription
                  sendAck.accept(communitySubscriptionMessage);
                } else {
                  log.error("message id:{} was not sent", communitySubscriptionMessage.id(), ex);
                }
              });
    } catch (Exception ex) {
      log.error("send error, value:{}", communitySubscriptionMessage, ex);
    }
  }
}
