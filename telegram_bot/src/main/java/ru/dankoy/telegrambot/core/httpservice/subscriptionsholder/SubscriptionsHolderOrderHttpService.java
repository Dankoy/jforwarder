package ru.dankoy.telegrambot.core.httpservice.subscriptionsholder;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import ru.dankoy.telegrambot.core.domain.subscription.Order;

@HttpExchange(url = "http://subscriptions-holder")
public interface SubscriptionsHolderOrderHttpService {

  @GetExchange(url = "/api/v1/orders")
  List<Order> getAllOrders();

  @GetExchange(url = "/api/v1/orders")
  List<Order> getOrdersByType(@RequestParam String subscriptionType);

  @GetExchange(url = "/api/v1/orders")
  Order getOrderByValueAndType(@RequestParam String value, @RequestParam String subscriptionType);
}
