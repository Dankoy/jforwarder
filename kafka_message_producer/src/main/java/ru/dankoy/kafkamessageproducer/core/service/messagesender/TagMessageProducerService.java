package ru.dankoy.kafkamessageproducer.core.service.messagesender;

import ru.dankoy.kafkamessageproducer.core.domain.message.TagSubscriptionMessage;

/**
 * @deprecated in favor {@link MessageProducerServiceKafka}
 */
@Deprecated(since = "2024-02-14")
public interface TagMessageProducerService {

  void send(TagSubscriptionMessage communitySubscriptionMessage);
}
