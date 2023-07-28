package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.tag.Order;

public interface OrderService {

  Order getByName(String name);

}
