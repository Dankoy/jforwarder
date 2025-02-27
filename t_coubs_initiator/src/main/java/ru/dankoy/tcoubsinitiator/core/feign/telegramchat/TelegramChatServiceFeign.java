package ru.dankoy.tcoubsinitiator.core.feign.telegramchat;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.Chat;

@FeignClient(contextId = "telegram-chat", name = "telegram-chat-service")
public interface TelegramChatServiceFeign {

  @GetMapping(
      path = "/api/v1/telegram_chat",
      params = {"page", "size", "sort", "search"})
  Chat getAllTelegramChats(Pageable pageable, @RequestParam("search") String search);
}
