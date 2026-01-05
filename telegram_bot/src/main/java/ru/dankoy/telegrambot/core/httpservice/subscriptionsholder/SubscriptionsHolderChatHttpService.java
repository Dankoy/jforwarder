package ru.dankoy.telegrambot.core.httpservice.subscriptionsholder;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;
import ru.dankoy.telegrambot.core.domain.Chat;

@HttpExchange(url = "http://subscriptions-holder")
public interface SubscriptionsHolderChatHttpService {

  /**
   * @deprecated for topics support via messageThreadId
   */
  @Deprecated(since = "2024-04-05", forRemoval = true)
  @GetExchange(url = "/api/v1/telegram_chat/{chatId}")
  Chat getChatById(@PathVariable("chatId") long chatId);

  /**
   * @deprecated chat is in separate microservice and db
   */
  @Deprecated(since = "2025-02-28", forRemoval = false)
  @GetExchange(url = "/api/v1/telegram_chat")
  Chat getChatByIdAndMessageThreadId(
      @RequestParam("chatId") long chatId,
      @RequestParam("messageThreadId") Integer messageThreadId);

  /**
   * @deprecated chat is in separate microservice and db
   */
  @Deprecated(since = "2025-02-28", forRemoval = false)
  @PostExchange(url = "/api/v1/telegram_chat")
  Chat createChat(@RequestBody Chat chat);

  /**
   * @deprecated chat is in separate microservice and db
   */
  @Deprecated(since = "2025-02-28", forRemoval = false)
  @PutExchange(url = "/api/v1/telegram_chat/{id}")
  Chat updateChat(@PathVariable("id") long id, @RequestBody Chat chat);
}
