package ru.dankoy.kafkamessageconsumer.core.service.subscription;

import ru.dankoy.kafkamessageconsumer.core.domain.message.CoubMessage;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Subscription;

public interface SubscriptionService {

  Subscription updatePermalink(CoubMessage coubMessage);
}
