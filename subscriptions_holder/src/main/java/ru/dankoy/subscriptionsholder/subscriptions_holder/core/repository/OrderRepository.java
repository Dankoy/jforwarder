package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import feign.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

  @Override
  @EntityGraph(value = "order-full")
  List<Order> findAll();

  @EntityGraph(value = "order-full")
  List<Order> findAllBySubscriptionTypeType(@Param("subscriptionType") String subscriptionType);

  @Query(
      """
      select o, st from Order o
      join o.subscriptionType st
      where st.type = :subscriptionType
      and o.value = :value
      """)
  Optional<Order> findByValueAndSubscriptionType(
      @Param("value") String value, @Param("subscriptionType") String subscriptionType);
}
