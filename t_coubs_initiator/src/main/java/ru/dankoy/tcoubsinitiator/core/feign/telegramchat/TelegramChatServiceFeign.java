package ru.dankoy.tcoubsinitiator.core.feign.telegramchat;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.dankoy.tcoubsinitiator.core.domain.telegramchatservice.Chat;
import ru.dankoy.tcoubsinitiator.core.domain.telegramchatservice.filter.TelegramChatFilter;

/**
 * @deprecated since spring-boot 4.0.0 in favor {@link ChannelSubscriptionServiceHttpClient}
 */
@Deprecated(since = "2025-01-04", forRemoval = true)
@FeignClient(contextId = "telegram-chat", name = "telegram-chat-service")
public interface TelegramChatServiceFeign {

  @GetMapping(
      path = "/api/v1/telegram_chat",
      params = {"page", "size", "sort"})
  Page<Chat> getAllTelegramChats(
      Pageable pageable, @SpringQueryMap @ModelAttribute TelegramChatFilter filter);
}
