package ru.dankoy.kafkamessageproducer.core.service.messagesender;

import ru.dankoy.kafkamessageproducer.core.domain.message.SubscriptionMessage;

public interface MessageProducerService {

  void send(SubscriptionMessage subscriptionMessage);

}
