package ru.dankoy.telegrambot.core.feign.telegramchatservice;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dankoy.telegrambot.core.domain.ChatWithUUID;

/**
 * @deprecated since spring-boot 4.0.0 in favor {@link ChannelSubscriptionServiceHttpClient}
 */
@Deprecated(since = "2025-01-04", forRemoval = true)
@FeignClient(name = "telegram-chat-service")
public interface TelegramChatServiceFeign {

  // chats

  @GetMapping(
      path = "/api/v1/telegram_chat",
      params = {"chatId", "messageThreadId"})
  ChatWithUUID getChatByIdAndMessageThreadId(
      @RequestParam("chatId") long chatId,
      @RequestParam("messageThreadId") Integer messageThreadId);

  @PostMapping(path = "/api/v1/telegram_chat")
  ChatWithUUID createChat(@RequestBody ChatWithUUID chat);

  @PutMapping(path = "/api/v1/telegram_chat/{id}")
  ChatWithUUID updateChat(@PathVariable("id") UUID id, @RequestBody ChatWithUUID chat);
}
