package ru.dankoy.telegrambot.core.service.order;


import java.util.List;
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
}
