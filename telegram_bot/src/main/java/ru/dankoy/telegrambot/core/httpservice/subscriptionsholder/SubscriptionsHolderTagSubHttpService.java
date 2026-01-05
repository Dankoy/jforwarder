package ru.dankoy.telegrambot.core.httpservice.subscriptionsholder;

import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import ru.dankoy.telegrambot.core.domain.subscription.tag.TagSubscription;

@HttpExchange(url = "http://subscriptions-holder")
public interface SubscriptionsHolderTagSubHttpService {

  @GetExchange(url = "/api/v1/tag_subscriptions")
  List<TagSubscription> getAllTagSubscriptionsByChatUuid(@RequestParam("chatUuid") UUID chatUuid);

  @PostExchange(url = "/api/v1/tag_subscriptions")
  TagSubscription subscribeByTag(@RequestBody TagSubscription tagSubscription);

  @DeleteExchange(url = "/api/v1/tag_subscriptions")
  void unsubscribeByTag(@RequestBody TagSubscription tagSubscription);

  /**
   * @deprecated chat is in separate microservice and db
   */
  @Deprecated(since = "2025-02-28", forRemoval = false)
  @GetExchange(url = "/api/v1/tag_subscriptions")
  List<TagSubscription> getAllTagSubscriptionsByChatIdAndMessageThreadId(
      @RequestParam("telegramChatId") long telegramChatId,
      @RequestParam("messageThreadId") Integer messageThreadId);
}
