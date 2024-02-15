package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Order;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

  private long id;

  @NotEmpty private String name;

  @NotEmpty private String value;

  public static OrderDTO toDTO(Order order) {

    return new OrderDTO(order.getId(), order.getName(), order.getValue());
  }

  public static Order fromDTO(OrderDTO dto) {

    return new Order(dto.getId(), dto.getName(), dto.getValue(), null);
  }
}
