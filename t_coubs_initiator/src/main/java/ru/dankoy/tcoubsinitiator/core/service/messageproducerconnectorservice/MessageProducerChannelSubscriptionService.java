package ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice;

import java.util.List;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.channelsubscription.ChannelSubscription;

public interface MessageProducerChannelSubscriptionService {

    void sendChannelSubscriptionsData(List<ChannelSubscription> channelSubscriptions);
}
