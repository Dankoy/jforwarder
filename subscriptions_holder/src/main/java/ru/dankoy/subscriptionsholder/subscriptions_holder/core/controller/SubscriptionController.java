package ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller;


import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Subscription;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.subscription.SubscriptionCreateDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.subscription.SubscriptionDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.subscription.SubscriptionUpdatePermalinkDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.SubscriptionService;


@RequiredArgsConstructor
@RestController
public class SubscriptionController {

  private final SubscriptionService subscriptionService;

  @GetMapping(value = "/api/v1/subscriptions")
  public List<Subscription> getAll() {
    return subscriptionService.getAll();
  }

  @GetMapping(value = "/api/v1/subscriptions/{communityName}")
  public List<Subscription> getAllByCommunityName(
      @PathVariable(value = "communityName") String communityName) {
    return subscriptionService.getAllByCommunityName(communityName);
  }

  @GetMapping(value = "/api/v1/subscriptions/{telegramChatId}")
  public List<Subscription> getAllByTelegramChatId(
      @PathVariable(value = "telegramChatId") long chatId) {
    return subscriptionService.getAllByChatId(chatId);
  }

  @PostMapping(path = "/api/v1/subscriptions")
  public SubscriptionDTO subscribeChatToCommunity(@Valid @RequestBody SubscriptionCreateDTO dto) {

    // добавляет чат к существующему community и все
    // если чат существует в базе, то использует его
    // если чата нет в базе, то создает новую запись в таблице чатов

    var subscription = SubscriptionCreateDTO.fromDTO(dto);

    var s = subscriptionService.subscribeChatToCommunity(subscription);

    return SubscriptionDTO.toDTO(s);

  }


  @DeleteMapping(path = "/api/v1/subscriptions")
  @ResponseStatus(code = HttpStatus.ACCEPTED)
  public void unsubscribeChat(@Valid @RequestBody SubscriptionCreateDTO dto) {

    var subscription = SubscriptionCreateDTO.fromDTO(dto);

    subscriptionService.unsubscribeChatFromCommunity(subscription);

  }

  @PutMapping(path = "/api/v1/subscriptions")
  public SubscriptionDTO subscribeChatToCommunity(
      @Valid @RequestBody SubscriptionUpdatePermalinkDTO dto) {

    var subscription = SubscriptionUpdatePermalinkDTO.fromDTO(dto);

    var s = subscriptionService.updateLastPermalink(subscription);

    return SubscriptionDTO.toDTO(s);

  }


}
