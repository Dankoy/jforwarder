package ru.dankoy.telegrambot.core.service.order;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.telegrambot.core.domain.subscription.Order;
import ru.dankoy.telegrambot.core.domain.subscription.SubscriptionType;
import ru.dankoy.telegrambot.core.feign.subscriptionsholder.SubscriptionsHolderFeign;

/**
 * @deprecated since spring-boot 4.0.0 in favor {@link ChannelSubscriptionServiceHttpClient}
 */
@Deprecated(since = "2025-01-04", forRemoval = true)
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

  private final SubscriptionsHolderFeign subscriptionsHolderFeign;

  @Override
  public List<Order> findAll() {
    return subscriptionsHolderFeign.getAllOrders();
  }

  @Override
  public List<Order> findAllByType(SubscriptionType subscriptionType) {
    return subscriptionsHolderFeign.getOrdersByType(subscriptionType.getType());
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
