package ru.dankoy.coubconnector.coub_connector.core.service.messageproducerconnectorservice;

import java.util.List;
import ru.dankoy.coubconnector.coub_connector.core.domain.subscribtionsholder.subscription.Subscription;

public interface MessageProducerService {

  void sendSubscriptionsData(List<Subscription> subscriptions);

}
