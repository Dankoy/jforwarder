package ru.dankoy.telegrambot.core.httpservice.subscriptionsholder;

import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import ru.dankoy.telegrambot.core.domain.subscription.community.CommunitySubscription;

@HttpExchange(url = "http://subscriptions-holder")
public interface SubscriptionsHolderCommunitySubHttpService {

  @GetExchange(url = "/api/v1/community_subscriptions")
  List<CommunitySubscription> getAllCommunitySubscriptionsByChatUuid(
      @RequestParam("chatUuid") UUID chatUuid);

  @PostExchange(url = "/api/v1/community_subscriptions")
  CommunitySubscription subscribe(@RequestBody CommunitySubscription communitySubscription);

  @DeleteExchange(url = "/api/v1/community_subscriptions")
  void unsubscribe(@RequestBody CommunitySubscription communitySubscription);

  /**
   * @deprecated chat is in separate microservice and db
   */
  // @Deprecated(since = "2025-02-28", forRemoval = false)
  @GetExchange(url = "/api/v1/community_subscriptions")
  List<CommunitySubscription> getAllSubscriptionsByChatIdAndMessageThreadId(
      @RequestParam("telegramChatId") long telegramChatId,
      @RequestParam("messageThreadId") Integer messageThreadId);
}
