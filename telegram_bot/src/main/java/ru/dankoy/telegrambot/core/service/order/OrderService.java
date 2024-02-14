package ru.dankoy.telegrambot.core.service.order;

import java.util.List;
import java.util.Optional;
import ru.dankoy.telegrambot.core.domain.subscription.Order;
import ru.dankoy.telegrambot.core.domain.subscription.SubscriptionType;

public interface OrderService {

    List<Order> findAll();

    List<Order> findAllByType(SubscriptionType subscriptionType);

    Optional<Order> findByValue(String value, SubscriptionType type);
}
