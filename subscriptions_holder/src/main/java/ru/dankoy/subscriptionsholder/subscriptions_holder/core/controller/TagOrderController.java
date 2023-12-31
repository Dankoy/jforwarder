package ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription.OrderDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.OrderService;

@RequiredArgsConstructor
@RestController
public class TagOrderController {

  private final OrderService orderService;

  @GetMapping(value = "/api/v1/tag_orders")
  public List<OrderDTO> getAll() {

    var found = orderService.getAll();

    return found.stream().map(OrderDTO::toDTO).toList();
  }


}
