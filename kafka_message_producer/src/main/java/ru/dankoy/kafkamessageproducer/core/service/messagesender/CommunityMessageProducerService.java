package ru.dankoy.kafkamessageproducer.core.service.messagesender;

import ru.dankoy.kafkamessageproducer.core.domain.message.CommunitySubscriptionMessage;

/**
 * @deprecated in favor {@link MessageProducerServiceKafka}
 */
@Deprecated(since = "2024-02-14")
public interface CommunityMessageProducerService {

  void send(CommunitySubscriptionMessage communitySubscriptionMessage);

}
