package ru.dankoy.subscriptions_scheduler.core.httpservice;

import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PutExchange;
import ru.dankoy.subscriptions_scheduler.core.domain.page.RestPageImpl;
import ru.dankoy.subscriptions_scheduler.core.dto.telegramchatservice.ChatWithUuidDTO;

@HttpExchange(url = "http://telegram-chat-service")
public interface TelegramChatHttpService {

  @GetExchange(url = "/api/v1/telegram_chat")
  RestPageImpl<ChatWithUuidDTO> getAllChats(
      @RequestParam(value = "search", required = true) String search,
      @RequestParam Pageable pageable);

  @PutExchange(url = "/api/v1/telegram_chat/{id}")
  ChatWithUuidDTO updateChat(@PathVariable("id") UUID id, @RequestBody ChatWithUuidDTO chat);
}
