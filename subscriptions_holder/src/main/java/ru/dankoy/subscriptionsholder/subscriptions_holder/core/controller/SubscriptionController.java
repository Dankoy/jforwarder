package ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.SubscriptionUpdatePermalinkDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.SubscriptionService;

@RequiredArgsConstructor
@RestController
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PutMapping("/api/v1/subscriptions")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SubscriptionUpdatePermalinkDTO updatePermalink(
            @Valid @RequestBody SubscriptionUpdatePermalinkDTO dto) {

        var ts = SubscriptionUpdatePermalinkDTO.fromDTO(dto);
        var sub = subscriptionService.updatePermalink(ts);

        return SubscriptionUpdatePermalinkDTO.toDTO(sub);
    }
}
