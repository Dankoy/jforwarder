package ru.dankoy.tcoubsinitiator.core.service.subscription;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.communitysubscription.CommunitySubscription;
import ru.dankoy.tcoubsinitiator.core.httpservice.subscription.SubscriptionHttpService;

@Primary
@Service
@RequiredArgsConstructor
public class SubscriptionServiceHttpClient implements SubscriptionService {

  private final SubscriptionHttpService subscriptionHttpService;

  @Override
  public Page<CommunitySubscription> getAllSubscriptions(Pageable pageable) {
    return subscriptionHttpService.getAllSubscriptions(pageable);
  }

  @Override
  public Page<CommunitySubscription> getAllSubscriptionsWithActiveChats(Pageable pageable) {
    return subscriptionHttpService.getAllSubscriptionsWithActiveChats(true, pageable);
  }

  @Override
  public Page<CommunitySubscription> getAllSubscriptionsByChatUuid(
      List<UUID> chatUuids, Pageable pageable) {
    return subscriptionHttpService.getCommunitySubscriptionByChatUuids(chatUuids, pageable);
  }
}
