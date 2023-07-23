package ru.dankoy.kafkamessageproducer.core.service.subscription;

import ru.dankoy.kafkamessageproducer.core.domain.message.SubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Subscription;

public interface SubscriptionService {

  Subscription updatePermalink(SubscriptionMessage subscription);

}
