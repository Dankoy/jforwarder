package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.HashMap;
import java.util.Map;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.SubscriptionType;

public interface SubscriptionTypeMaker {

  default Map<String, SubscriptionType> makeCorrectSubscriptionType() {

    Map<String, SubscriptionType> subs = new HashMap<>();
    subs.put("community", new SubscriptionType(1L, "community"));
    subs.put("tag", new SubscriptionType(2L, "tag"));
    subs.put("channel", new SubscriptionType(3L, "channel"));

    return subs;
  }
}
