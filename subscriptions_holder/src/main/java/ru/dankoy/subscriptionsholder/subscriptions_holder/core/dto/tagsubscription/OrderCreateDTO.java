package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.tag.Order;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDTO {

  @NotEmpty
  private String value;

  public static OrderCreateDTO toDTO(Order order) {

    return new OrderCreateDTO(
        order.getValue()
    );

  }

  public static Order fromDTO(OrderCreateDTO dto) {

    return new Order(
        0,
        null,
        dto.getValue()
    );

  }


}
