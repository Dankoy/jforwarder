package ru.dankoy.tcoubsinitiator.core.service.channelsubscription;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.channelsubscription.ChannelSubscription;
import ru.dankoy.tcoubsinitiator.core.feign.subscription.SubscriptionFeign;

/**
 * @deprecated since spring-boot 4.0.0 in favor {@link ChannelSubscriptionServiceHttpClient}
 */
@Deprecated(since = "2025-01-04", forRemoval = true)
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
