package ru.dankoy.telegrambot.core.httpservice.telegramchatservice;

import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;
import ru.dankoy.telegrambot.core.domain.ChatWithUUID;

@HttpExchange(url = "http://telegram-chat-service")
public interface TelegramChatHttpService {

  @GetExchange(url = "/api/v1/telegram_chat")
  ChatWithUUID getChatByIdAndMessageThreadId(
      @RequestParam("chatId") long chatId,
      @RequestParam("messageThreadId") Integer messageThreadId);

  @PostExchange(url = "/api/v1/telegram_chat")
  ChatWithUUID createChat(@RequestBody ChatWithUUID chat);

  @PutExchange(url = "/api/v1/telegram_chat/{id}")
  ChatWithUUID updateChat(@PathVariable("id") UUID id, @RequestBody ChatWithUUID chat);
}
