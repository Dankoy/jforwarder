package ru.dankoy.kafkamessageproducer.core.service.messagesender;

import ru.dankoy.kafkamessageproducer.core.domain.message.ChannelSubscriptionMessage;

public interface ChannelMessageProducerService {

  void send(ChannelSubscriptionMessage channelSubscriptionMessage);
}
