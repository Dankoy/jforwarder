package ru.dankoy.tcoubsinitiator.core.service.subscription;

import java.util.List;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.communitysubscription.CommunitySubscription;

public interface SubscriptionService {

  List<CommunitySubscription> getAllSubscriptions();

  List<CommunitySubscription> getAllSubscriptionsWithActiveChats();
}
