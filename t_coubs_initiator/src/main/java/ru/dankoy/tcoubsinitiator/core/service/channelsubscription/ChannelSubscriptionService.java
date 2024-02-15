package ru.dankoy.tcoubsinitiator.core.service.channelsubscription;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.channelsubscription.ChannelSubscription;

public interface ChannelSubscriptionService {

  Page<ChannelSubscription> getAllSubscriptionsWithActiveChats(Pageable pageable);
}
