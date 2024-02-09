package ru.dankoy.telegrambot.core.service.order;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.telegrambot.core.domain.tagsubscription.Order;
import ru.dankoy.telegrambot.core.feign.subscriptionsholder.SubscriptionsHolderFeign;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

  private final SubscriptionsHolderFeign subscriptionsHolderFeign;

  @Override
  public List<Order> findAll() {
    return subscriptionsHolderFeign.getAllTagOrders();
  }

  @Override
  public Optional<Order> findByValue(String value) {
    try {
      return Optional.of(subscriptionsHolderFeign.getOrderByValue(value));
    } catch (Exception e) {
      return Optional.empty();
    }
  }
}
