package ru.dankoy.kafkamessageproducer.core.service.messagesender.channel;

import ru.dankoy.kafkamessageproducer.core.domain.message.ChannelSubscriptionMessage;

/**
 * @deprecated in favor {@link MessageProducerServiceKafka}
 */
@Deprecated(since = "2024-02-14")
public interface ChannelMessageProducerService {

  void send(ChannelSubscriptionMessage channelSubscriptionMessage);
}
