package ru.dankoy.coubconnector.coub_connector.core.service.subscription;

import java.util.List;
import ru.dankoy.coubconnector.coub_connector.core.domain.subscribtionsholder.subscription.Subscription;

public interface SubscriptionService {

  List<Subscription> getAllSubscriptions();

}
