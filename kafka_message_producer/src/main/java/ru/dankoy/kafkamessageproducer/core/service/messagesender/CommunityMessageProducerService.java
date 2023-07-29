package ru.dankoy.kafkamessageproducer.core.service.messagesender;

import ru.dankoy.kafkamessageproducer.core.domain.message.CommunitySubscriptionMessage;

public interface CommunityMessageProducerService {

  void send(CommunitySubscriptionMessage communitySubscriptionMessage);

}
