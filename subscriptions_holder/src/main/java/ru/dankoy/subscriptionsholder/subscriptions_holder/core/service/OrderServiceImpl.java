package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Order;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.OrderRepository;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;

  @Override
  public Order getByName(String name) {
    var optional = orderRepository.findByName(name);

    return optional.orElseThrow(
        () -> new ResourceNotFoundException(String.format("Tag order not found - %s", name))
    );
  }

  @Override
  public Order getByValue(String value) {
    var optional = orderRepository.findByValue(value);

    return optional.orElseThrow(
        () -> new ResourceNotFoundException(String.format("Tag order not found - %s", value))
    );
  }

  @Override
  public List<Order> getAll() {
    return orderRepository.findAll();
  }
}
