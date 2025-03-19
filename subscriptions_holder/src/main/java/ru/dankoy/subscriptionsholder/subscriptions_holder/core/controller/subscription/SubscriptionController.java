package ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller.subscription;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.SubscriptionDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.SubscriptionUpdatePermalinkDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.SubscriptionWithoutChatDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.subscription.SubscriptionService;

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

  @GetMapping(value = "/api/v1/subscriptions")
  public Page<SubscriptionDTO> getChats(Pageable pageable) {

    var page = subscriptionService.findAll(pageable);

    return page.map(SubscriptionDTO::toDTO);
  }

  @GetMapping(
      value = "/api/v1/subscriptions",
      params = {"chatUuids"})
  public Page<SubscriptionWithoutChatDTO> getChats(
      @RequestParam(value = "chatUuids") List<UUID> chatUuids, Pageable pageable) {

    var page = subscriptionService.findAllByChatsUUID(chatUuids, pageable);

    return page.map(SubscriptionWithoutChatDTO::toDTO);
  }
}
