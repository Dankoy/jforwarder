package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.time.LocalDateTime;
import java.util.Set;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.registry.SentCoubsRegistry;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Subscription;

public interface SentCoubsRegistryMaker {

  default Set<SentCoubsRegistry> makeCorrectRegistry(Subscription subscription) {

    var date = LocalDateTime.now();
    var permalink = "pl";

    var r = new SentCoubsRegistry(0L, subscription, permalink, date);

    return Set.of(r);
  }
}
