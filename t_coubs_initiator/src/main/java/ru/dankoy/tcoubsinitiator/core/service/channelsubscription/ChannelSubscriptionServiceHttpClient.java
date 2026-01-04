package ru.dankoy.tcoubsinitiator.core.service.channelsubscription;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.channelsubscription.ChannelSubscription;
import ru.dankoy.tcoubsinitiator.core.httpservice.subscription.SubscriptionHttpService;

@Primary
@Service
@RequiredArgsConstructor
public class ChannelSubscriptionServiceHttpClient implements ChannelSubscriptionService {

  private final SubscriptionHttpService subscriptionHttpService;

  @Override
  public Page<ChannelSubscription> getAllSubscriptionsWithActiveChats(Pageable pageable) {
    return subscriptionHttpService.getAllChannelSubscriptionsWithActiveChats(true, pageable);
  }

  @Override
  public Page<ChannelSubscription> getAllSubscriptionsByChatUuid(
      List<UUID> chatUuids, Pageable pageable) {
    return subscriptionHttpService.getChannelSubscriptionByChatUuids(chatUuids, pageable);
  }
}
