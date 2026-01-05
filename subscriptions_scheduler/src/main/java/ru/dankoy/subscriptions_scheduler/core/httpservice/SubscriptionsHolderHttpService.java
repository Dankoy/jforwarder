package ru.dankoy.subscriptions_scheduler.core.httpservice;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PutExchange;
import ru.dankoy.subscriptions_scheduler.core.domain.page.RestPageImpl;
import ru.dankoy.subscriptions_scheduler.core.domain.subscribtionsholder.Chat;
import ru.dankoy.subscriptions_scheduler.core.dto.subscriptions.ChatDTO;
import ru.dankoy.subscriptions_scheduler.core.dto.subscriptions.ChatWithSubsDTO;
import ru.dankoy.subscriptions_scheduler.core.dto.subscriptions.SubscriptionDTO;

@HttpExchange(url = "http://subscriptions-holder")
public interface SubscriptionsHolderHttpService {

  @GetExchange(url = "/api/v1/telegram_chat")
  RestPageImpl<ChatWithSubsDTO> getAllChats(
      @RequestParam("with_subs") boolean withSubs,
      @RequestParam(value = "search", required = false) String search,
      @RequestParam Pageable pageable);

  @PutExchange(url = "/api/v1/telegram_chat/{id}")
  Chat updateChat(@PathVariable("id") long id, @RequestBody ChatDTO chat);

  @GetExchange(url = "/api/v1/subscriptions")
  RestPageImpl<SubscriptionDTO> getSubscriptionsFiltered(
      @RequestParam(value = "chatUuids", required = true) List<UUID> chatUuids,
      @RequestParam Pageable pageable);
}
