package ru.dankoy.kafkamessageproducer.core.service.messagesender;

import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import ru.dankoy.kafkamessageproducer.core.domain.message.SubscriptionMessage;

@Slf4j
@RequiredArgsConstructor
public class MessageProducerServiceKafka implements MessageProducerService {

  private final KafkaTemplate<String, SubscriptionMessage> kafkaTemplate;

  private final String topic;

  private final Consumer<SubscriptionMessage> sendAck;


  @Override
  public void send(SubscriptionMessage subscriptionMessage) {
    try {
      log.info("message: {}", subscriptionMessage);
      kafkaTemplate.send(topic, subscriptionMessage)
          .whenComplete(
              (result, ex) -> {
                if (ex == null) {
                  log.info(
                      "message id: {} was sent, offset: {}",
                      subscriptionMessage.id(),
                      result.getRecordMetadata().offset()
                  );
                  // if kafka accepted - send update last permalink in db for subscription
                  sendAck.accept(subscriptionMessage);
                } else {
                  log.error("message id:{} was not sent", subscriptionMessage.id(), ex);
                }
              });
    } catch (Exception ex) {
      log.error("send error, value:{}", subscriptionMessage, ex);
    }
  }
}
