package ru.dankoy.kafkamessageproducer.core.service.messagesender;

import ru.dankoy.kafkamessageproducer.core.domain.message.TagSubscriptionMessage;

public interface TagMessageProducerService {

  void send(TagSubscriptionMessage communitySubscriptionMessage);

}
