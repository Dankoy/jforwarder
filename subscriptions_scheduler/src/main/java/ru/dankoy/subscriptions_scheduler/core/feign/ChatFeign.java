package ru.dankoy.subscriptions_scheduler.core.feign;

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

@FeignClient(contextId = "telegram-chat", name = "subscriptions-holder")
public interface ChatFeign {

  @GetMapping(
      path = "/api/v1/telegram_chat",
      params = {"page", "size", "sort", "with_subs"})
  Page<ChatWithSubsDTO> getAllChats(@RequestParam("with_subs") boolean withSubs, Pageable pageable);

  @PutMapping(path = "/api/v1/telegram_chat/{id}")
  Chat updateChat(@PathVariable("id") long id, @RequestBody ChatDTO chat);
}
