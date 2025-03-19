package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.order;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Order;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.order.OrderRepository;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;

  @Override
  public Order getByValueAndType(String value, String subscriptionType) {
    var optional = orderRepository.findByValueAndSubscriptionType(value, subscriptionType);

    return optional.orElseThrow(
        () -> new ResourceNotFoundException(String.format("Order not found - %s", value)));
  }

  @Override
  public List<Order> getAll() {
    return orderRepository.findAll();
  }

  @Override
  public List<Order> getAllBySubscriptionType(String subscriptionType) {
    return orderRepository.findAllBySubscriptionTypeType(subscriptionType);
  }
}
