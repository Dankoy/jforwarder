package ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription.OrderDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.OrderService;

@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    @GetMapping(value = "/api/v1/orders")
    public @Valid List<OrderDTO> getAll() {

        var found = orderService.getAll();

        return found.stream().map(OrderDTO::toDTO).toList();
    }

    @GetMapping(
            value = "/api/v1/orders",
            params = {"subscriptionType"})
    public @Valid List<OrderDTO> getAll(@RequestParam String subscriptionType) {

        var found = orderService.getAllBySubscriptionType(subscriptionType);

        return found.stream().map(OrderDTO::toDTO).toList();
    }

    @GetMapping(
            value = "/api/v1/orders",
            params = {"value", "subscriptionType"})
    public @Valid OrderDTO getByValueAndSubscriptionType(
            @RequestParam String value, @RequestParam String subscriptionType) {

        var found = orderService.getByValueAndType(value, subscriptionType);

        return OrderDTO.toDTO(found);
    }
}
