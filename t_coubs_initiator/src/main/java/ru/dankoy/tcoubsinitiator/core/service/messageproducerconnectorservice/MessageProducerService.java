package ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice;

import java.util.List;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.subscription.Subscription;

public interface MessageProducerService {

  void sendSubscriptionsData(List<Subscription> subscriptions);

}
