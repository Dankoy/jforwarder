package ru.dankoy.kafkamessageproducer.core.service.converter;

import java.util.List;
import ru.dankoy.kafkamessageproducer.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.tagsubscription.TagSubscription;

public interface TagMessageConverter {

  List<TagSubscriptionMessage> convert(TagSubscription tagSubscription);

  TagSubscription convert(TagSubscriptionMessage tagSubscriptionMessage);
}
