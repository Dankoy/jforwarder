package ru.dankoy.tcoubsinitiator.core.httpservice.telegramchat;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import ru.dankoy.tcoubsinitiator.core.domain.page.RestPageImpl;
import ru.dankoy.tcoubsinitiator.core.domain.telegramchatservice.Chat;
import ru.dankoy.tcoubsinitiator.core.domain.telegramchatservice.filter.TelegramChatFilter;

@HttpExchange(url = "http://telegram-chat-service")
public interface TelegramChatHttpService {

  @GetExchange(url = "/api/v1/telegram_chat")
  RestPageImpl<Chat> getAllTelegramChats(
      @RequestParam Pageable pageable, @RequestParam TelegramChatFilter filter);
}
