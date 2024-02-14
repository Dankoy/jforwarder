package ru.dankoy.kafkamessageproducer.core.service.subscription;

import ru.dankoy.kafkamessageproducer.core.domain.message.CoubMessage;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Subscription;

public interface SubscriptionService {

    Subscription updatePermalink(CoubMessage coubMessage);
}
