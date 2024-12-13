package ru.dankoy.subscriptionsholder.subscriptions_holder.core.jpalisteners;

import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Subscription;

public class SubscriptionListener {

  @PrePersist
  public void prePersist(Subscription subscription) {
    if (subscription.getCreatedAt() == null) {
      subscription.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
    }
  }
}
