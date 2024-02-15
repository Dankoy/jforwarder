package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {}
