package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Subscription;

public interface SubscriptionService {

  Subscription findById(long id);

  Subscription updatePermalink(Subscription subscription);
}
