package ru.dankoy.subscriptions_scheduler.core.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import ru.dankoy.subscriptions_scheduler.core.dto.subscriptions.ChatDTO;

@FeignClient(contextId = "telegram-chat", name = "subscriptions-holder")
public interface ChatFeign {

  @GetMapping(
      path = "/api/v1/telegram_chat",
      params = {"page", "size", "sort"})
  Page<ChatDTO> getAllChats(Pageable pageable);
}
