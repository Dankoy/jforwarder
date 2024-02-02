package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.tag.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

  Optional<Order> findByName(String name);
  Optional<Order> findByValue(String value);

}
