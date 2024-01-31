package ru.dankoy.kafkamessageproducer.core.service.converter;

import java.util.List;
import ru.dankoy.kafkamessageproducer.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.tagsubscription.TagSubscription;

/**
 * @deprecated use {@link MessageConverter}
 */
@Deprecated(since = "2024-01-30")
public interface TagMessageConverter {

  List<TagSubscriptionMessage> convert(TagSubscription tagSubscription);

  TagSubscription convert(TagSubscriptionMessage tagSubscriptionMessage);
}
