package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.tag.Order;

public interface OrderService {

  Order getByName(String name);

  Order getByValue(String value);

  List<Order> getAll();
}
