package ru.dankoy.telegrambot.core.service.order;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.telegrambot.core.domain.Order;
import ru.dankoy.telegrambot.core.domain.SubscriptionType;
import ru.dankoy.telegrambot.core.feign.subscriptionsholder.SubscriptionsHolderFeign;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

  private final SubscriptionsHolderFeign subscriptionsHolderFeign;

  @Override
  public List<Order> findAll() {
    return subscriptionsHolderFeign.getAllOrders();
  }

  @Override
  public Optional<Order> findByValue(String value, SubscriptionType type) {
    try {
      return Optional.of(subscriptionsHolderFeign.getOrderByValueAndType(value, type.getType()));
    } catch (Exception e) {
      return Optional.empty();
    }
  }
}
