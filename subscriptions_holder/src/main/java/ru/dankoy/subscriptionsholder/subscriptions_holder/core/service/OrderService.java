package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Order;

public interface OrderService {

    Order getByValueAndType(String value, String subscriptionType);

    List<Order> getAll();

    List<Order> getAllBySubscriptionType(String subscriptionType);
}
