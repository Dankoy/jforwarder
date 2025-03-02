package ru.dankoy.subscriptions_scheduler.core.feign;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dankoy.subscriptions_scheduler.core.dto.telegramchatservice.ChatWithUuidDTO;

@FeignClient(contextId = "telegram-chat", name = "telegram-chat-service")
public interface TelegramChatServiceFeign {

  @GetMapping(
      path = "/api/v1/telegram_chat",
      params = {"page", "size", "sort", "search"})
  Page<ChatWithUuidDTO> getAllChats(
      @RequestParam(value = "search", required = true) String search, Pageable pageable);

  @PutMapping(path = "/api/v1/telegram_chat/{id}")
  ChatWithUuidDTO updateChat(@PathVariable("id") UUID id, @RequestBody ChatWithUuidDTO chat);
}
