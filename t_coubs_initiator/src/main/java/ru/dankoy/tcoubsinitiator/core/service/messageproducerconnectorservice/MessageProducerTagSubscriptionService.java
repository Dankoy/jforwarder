package ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice;

import java.util.List;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.tagsubscription.TagSubscription;

public interface MessageProducerTagSubscriptionService {

    void sendTagSubscriptionsData(List<TagSubscription> tagSubscriptions);
}
