package ru.dankoy.tcoubsinitiator.core.service.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.communitysubscription.CommunitySubscription;
import ru.dankoy.tcoubsinitiator.core.feign.subscription.SubscriptionFeign;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

  private final SubscriptionFeign subscriptionFeign;

  @Override
  public Page<CommunitySubscription> getAllSubscriptions(Pageable pageable) {
    return subscriptionFeign.getAllSubscriptions(pageable);
  }

  @Override
  public Page<CommunitySubscription> getAllSubscriptionsWithActiveChats(Pageable pageable) {
    return subscriptionFeign.getAllSubscriptionsWithActiveChats(true, pageable);
  }
}
