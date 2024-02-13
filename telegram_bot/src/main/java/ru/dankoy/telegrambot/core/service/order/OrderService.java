package ru.dankoy.telegrambot.core.service.order;

import java.util.List;
import java.util.Optional;
import ru.dankoy.telegrambot.core.domain.Order;
import ru.dankoy.telegrambot.core.domain.SubscriptionType;

public interface OrderService {

  List<Order> findAll();

  Optional<Order> findByValue(String value, SubscriptionType type);
}
