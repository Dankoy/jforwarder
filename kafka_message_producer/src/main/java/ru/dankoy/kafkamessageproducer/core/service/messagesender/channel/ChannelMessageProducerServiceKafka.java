package ru.dankoy.kafkamessageproducer.core.service.messagesender.channel;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import ru.dankoy.kafkamessageproducer.core.domain.message.ChannelSubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.message.CoubMessage;

/**
 * @deprecated in favor {@link MessageProducerServiceKafka}
 */
@Deprecated(since = "2024-02-14")
@Slf4j
@RequiredArgsConstructor
public class ChannelMessageProducerServiceKafka implements ChannelMessageProducerService {

  private final KafkaTemplate<String, CoubMessage> kafkaTemplate;

  private final String topic;

  private final Consumer<ChannelSubscriptionMessage> sendAck;

  private final Consumer<ChannelSubscriptionMessage> sendAckToRegistry;

  @Override
  public void send(ChannelSubscriptionMessage channelSubscriptionMessage) {
    try {
      log.info("message: {}", channelSubscriptionMessage);

      ProducerRecord<String, CoubMessage> producerRecord =
          new ProducerRecord<>(topic, channelSubscriptionMessage);

      producerRecord
          .headers()
          .add("subscription_type", "BY_CHANNEL".getBytes(StandardCharsets.UTF_8));

      kafkaTemplate
          .send(producerRecord)
          .whenComplete(
              (result, ex) -> {
                if (ex == null) {
                  log.info(
                      "message id: {} was sent, offset: {}",
                      channelSubscriptionMessage.getId(),
                      result.getRecordMetadata().offset());
                  // if kafka accepted - send update last permalink in db for
                  // subscription
                  sendAck.accept(channelSubscriptionMessage);
                  // update registry
                  sendAckToRegistry.accept(channelSubscriptionMessage);
                  log.info("acknowledgement sent for {}", channelSubscriptionMessage);
                } else {
                  log.error("message id:{} was not sent", channelSubscriptionMessage.getId(), ex);
                }
              });
    } catch (Exception ex) {
      log.error("send error, value:{}", channelSubscriptionMessage, ex);
    }
  }
}
