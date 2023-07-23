package ru.dankoy.kafkamessageproducer.core.service.converter;

import java.util.List;
import ru.dankoy.kafkamessageproducer.core.domain.message.SubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Subscription;

public interface MessageConverter {

  List<SubscriptionMessage> convert(Subscription subscription);

  Subscription convert(SubscriptionMessage subscriptionMessage);
}
