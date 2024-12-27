package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Subscription;

public interface SubscriptionService {

  Page<Subscription> findAll(Pageable pageable);

  Subscription findById(long id);

  Subscription updatePermalink(Subscription subscription);
}
