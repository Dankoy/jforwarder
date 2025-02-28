package ru.dankoy.tcoubsinitiator.core.service.channelsubscription;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.channelsubscription.ChannelSubscription;
import ru.dankoy.tcoubsinitiator.core.feign.subscription.SubscriptionFeign;

@Service
@RequiredArgsConstructor
public class ChannelSubscriptionServiceImpl implements ChannelSubscriptionService {

  private final SubscriptionFeign subscriptionFeign;

  @Override
  public Page<ChannelSubscription> getAllSubscriptionsWithActiveChats(Pageable pageable) {
    return subscriptionFeign.getAllChannelSubscriptionsWithActiveChats(true, pageable);
  }

  @Override
  public Page<ChannelSubscription> getAllSubscriptionsByChatUuid(
      List<UUID> chatUuids, Pageable pageable) {
    return subscriptionFeign.getChannelSubscriptionByChatUuids(chatUuids, pageable);
  }
}
