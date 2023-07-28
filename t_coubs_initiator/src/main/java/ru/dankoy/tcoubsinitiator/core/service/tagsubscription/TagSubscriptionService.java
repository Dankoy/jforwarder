package ru.dankoy.tcoubsinitiator.core.service.tagsubscription;

import java.util.List;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.tagsubscription.TagSubscription;

public interface TagSubscriptionService {

  List<TagSubscription> getAllSubscriptions(String tag);

  List<TagSubscription> getAllSubscriptionsWithActiveChats();
}
