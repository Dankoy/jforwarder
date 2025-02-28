package ru.dankoy.tcoubsinitiator.core.service.channelsubscription;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.channelsubscription.ChannelSubscription;

public interface ChannelSubscriptionService {

  Page<ChannelSubscription> getAllSubscriptionsWithActiveChats(Pageable pageable);

  Page<ChannelSubscription> getAllSubscriptionsByChatUuid(List<UUID> chatUuids, Pageable pageable);
}
