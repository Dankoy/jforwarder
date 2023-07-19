package ru.dankoy.tcoubsinitiator.core.service.subscription;

import java.util.List;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.subscription.Subscription;

public interface SubscriptionService {

  List<Subscription> getAllSubscriptions();

}
