package ru.dankoy.subscriptions_scheduler.core.feign;

import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dankoy.subscriptions_scheduler.core.domain.subscribtionsholder.Chat;
import ru.dankoy.subscriptions_scheduler.core.dto.subscriptions.ChatDTO;
import ru.dankoy.subscriptions_scheduler.core.dto.subscriptions.ChatWithSubsDTO;
import ru.dankoy.subscriptions_scheduler.core.dto.subscriptions.SubscriptionDTO;

@FeignClient(contextId = "subscriptions-holder", name = "subscriptions-holder")
public interface SubscriptionsHolderFeign {

  @GetMapping(
      path = "/api/v1/telegram_chat",
      params = {"page", "size", "sort", "with_subs", "search"})
  Page<ChatWithSubsDTO> getAllChats(
      @RequestParam("with_subs") boolean withSubs,
      @RequestParam(value = "search", required = false) String search,
      Pageable pageable);

  @PutMapping(path = "/api/v1/telegram_chat/{id}")
  Chat updateChat(@PathVariable("id") long id, @RequestBody ChatDTO chat);

  @GetMapping(
      path = "/api/v1/subscriptions",
      params = {"page", "size", "sort", "chatUuids"})
  Page<SubscriptionDTO> getSubscriptionsFiltered(
      @RequestParam(value = "chatUuids", required = true) List<UUID> chatUuids, Pageable pageable);
}
