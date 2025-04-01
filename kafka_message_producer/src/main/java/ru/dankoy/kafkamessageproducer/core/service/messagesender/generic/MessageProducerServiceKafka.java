package ru.dankoy.kafkamessageproducer.core.service.messagesender.generic;

import ru.dankoy.kafkamessageproducer.core.domain.message.CoubMessage;

public interface MessageProducerServiceKafka {

  void send(CoubMessage message);
}
