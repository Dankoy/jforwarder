package ru.dankoy.telegrambot.core.service.order;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.telegrambot.core.domain.subscription.Order;
import ru.dankoy.telegrambot.core.domain.subscription.SubscriptionType;
import ru.dankoy.telegrambot.core.httpservice.subscriptionsholder.SubscriptionsHolderOrderHttpService;

@RequiredArgsConstructor
@Service("orderServiceHttpClient")
public class OrderServiceHttpClient implements OrderService {

  private final SubscriptionsHolderOrderHttpService subscriptionsHolderOrderHttpService;

  @Override
  public List<Order> findAll() {
    return subscriptionsHolderOrderHttpService.getAllOrders();
  }

  @Override
  public List<Order> findAllByType(SubscriptionType subscriptionType) {
    return subscriptionsHolderOrderHttpService.getOrdersByType(subscriptionType.getType());
  }

  @Override
  public Optional<Order> findByValue(String value, SubscriptionType type) {
    try {
      return Optional.of(
          subscriptionsHolderOrderHttpService.getOrderByValueAndType(value, type.getType()));
    } catch (Exception e) {
      return Optional.empty();
    }
  }
}
