package ru.dankoy.telegrambot.core.httpservice.subscriptionsholder;

import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import ru.dankoy.telegrambot.core.domain.subscription.channel.ChannelSubscription;

@HttpExchange(url = "http://subscriptions-holder")
public interface SubscriptionsHolderChannelSubHttpService {

  @GetExchange(url = "/api/v1/channel_subscriptions")
  List<ChannelSubscription> getAllChannelSubscriptionsByChatUuid(
      @RequestParam("chatUuid") UUID chatUuid);

  @PostExchange(url = "/api/v1/channel_subscriptions")
  ChannelSubscription subscribeByChannel(@RequestBody ChannelSubscription channelSubscription);

  @DeleteExchange(url = "/api/v1/channel_subscriptions")
  void unsubscribeByChannel(@RequestBody ChannelSubscription channelSubscription);

  /**
   * @deprecated chat is in separate microservice and db
   */
  @Deprecated(since = "2025-02-28", forRemoval = false)
  @GetExchange(url = "/api/v1/channel_subscriptions")
  List<ChannelSubscription> getAllChannelSubscriptionsByChatIdAndMessageThreadId(
      @RequestParam("telegramChatId") long telegramChatId,
      @RequestParam("messageThreadId") Integer messageThreadId);
}
