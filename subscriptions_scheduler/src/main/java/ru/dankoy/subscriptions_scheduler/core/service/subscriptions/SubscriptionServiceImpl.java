package ru.dankoy.subscriptions_scheduler.core.service.subscriptions;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptions_scheduler.core.domain.subscribtionsholder.subscription.Subscription;
import ru.dankoy.subscriptions_scheduler.core.feign.SubscriptionsFeign;
import ru.dankoy.subscriptions_scheduler.core.mapper.SubscriptionMapper;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

  private final SubscriptionsFeign subscriptionsFeign;
  private final SubscriptionMapper subscriptionMapper;

  @Override
  public Page<Subscription> findAll(Pageable pageable) {
    return subscriptionsFeign.getAllChats(pageable).map(subscriptionMapper::fromDto);
  }
}
