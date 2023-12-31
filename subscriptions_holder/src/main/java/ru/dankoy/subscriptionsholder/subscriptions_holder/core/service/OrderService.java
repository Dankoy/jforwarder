package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.tag.Order;

public interface OrderService {

  Order getByName(String name);

  List<Order> getAll();
}
