package ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice;

import java.util.List;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.communitysubscription.CommunitySubscription;

public interface MessageProducerCommunitySubscriptionService {

    void sendCommunitySubscriptionsData(List<CommunitySubscription> communitySubscriptions);
}
