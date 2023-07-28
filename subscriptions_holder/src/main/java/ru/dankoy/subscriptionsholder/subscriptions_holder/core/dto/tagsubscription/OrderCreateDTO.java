package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.tag.Order;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDTO {

  private String name;

  public static OrderCreateDTO toDTO(Order order) {

    return new OrderCreateDTO(
        order.getName()
    );

  }

  public static Order fromDTO(OrderCreateDTO dto) {

    return new Order(
        0,
        dto.getName()
    );

  }


}
