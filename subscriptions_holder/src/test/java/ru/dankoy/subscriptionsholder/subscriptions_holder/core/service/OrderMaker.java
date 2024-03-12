package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Order;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.SubscriptionType;

public interface OrderMaker extends SubscriptionTypeMaker {

  default List<Order> makeCorrectOrders() {

    Map<String, SubscriptionType> subs = makeCorrectSubscriptionType();

    return Stream.of(
            new Order(1L, "likes_count", "top", subs.get("tag")),
            new Order(2L, "newest_popular", "popular", subs.get("tag")),
            new Order(3L, "views_count", "views_count", subs.get("tag")),
            new Order(4L, "newest", "fresh", subs.get("tag")),
            new Order(5L, "likes_count", "most_liked", subs.get("channel")),
            new Order(6L, "newest", "most_recent", subs.get("channel")),
            new Order(7L, "views_count", "most_viewed", subs.get("channel")),
            new Order(8L, "oldest", "oldest", subs.get("channel")))
        .toList();
  }

  default Order findCorrectByValueAndSubscriptionType(String value, String subscriptionType) {

    Optional<Order> expectedOptional =
        makeCorrectOrders().stream()
            .filter(
                c ->
                    c.getValue().equals(value)
                        && c.getSubscriptionType().getType().equals(subscriptionType))
            .findFirst();

    return expectedOptional.orElseThrow();
  }
}
