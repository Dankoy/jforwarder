package ru.dankoy.subscriptionsholder.subscriptions_holder.core.jpalisteners;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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

  @PreUpdate
  public void preUpdate(Subscription subscription) {
    subscription.setModifiedAt(LocalDateTime.now(ZoneOffset.UTC));
  }
}
