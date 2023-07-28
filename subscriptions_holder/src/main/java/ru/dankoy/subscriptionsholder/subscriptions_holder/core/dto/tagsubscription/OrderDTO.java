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
public class OrderDTO {

  private long id;

  private String name;

  public static OrderDTO toDTO(Order order) {

    return new OrderDTO(
        order.getId(),
        order.getName()
    );

  }

  public static Order fromDTO(OrderDTO dto) {

    return new Order(
        dto.getId(),
        dto.getName()
    );

  }


}
