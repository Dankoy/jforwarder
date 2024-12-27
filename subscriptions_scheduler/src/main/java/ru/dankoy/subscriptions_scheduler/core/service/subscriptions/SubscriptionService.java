package ru.dankoy.subscriptions_scheduler.core.service.subscriptions;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.subscriptions_scheduler.core.domain.subscribtionsholder.subscription.Subscription;

public interface SubscriptionService {

  Page<Subscription> findAll(Pageable pageable);
}
