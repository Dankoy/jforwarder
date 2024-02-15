package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.channelsub;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Order;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.SubscriptionType;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDTO {

  @NotEmpty private String value;

  @Valid @NotNull private SubscriptionTypeCreateDTO subscriptionType;

  public static OrderCreateDTO toDTO(Order order) {

    return new OrderCreateDTO(
        order.getValue(), new SubscriptionTypeCreateDTO(order.getSubscriptionType().getType()));
  }

  public static Order fromDTO(OrderCreateDTO dto) {

    return new Order(
        0, null, dto.getValue(), new SubscriptionType(0, dto.getSubscriptionType().getType()));
  }
}
