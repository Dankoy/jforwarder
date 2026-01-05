package ru.dankoy.telegrambot.core.feign.subscriptionsholder;

import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dankoy.telegrambot.core.domain.Chat;
import ru.dankoy.telegrambot.core.domain.subscription.Order;
import ru.dankoy.telegrambot.core.domain.subscription.channel.Channel;
import ru.dankoy.telegrambot.core.domain.subscription.channel.ChannelSubscription;
import ru.dankoy.telegrambot.core.domain.subscription.community.Community;
import ru.dankoy.telegrambot.core.domain.subscription.community.CommunitySubscription;
import ru.dankoy.telegrambot.core.domain.subscription.tag.Tag;
import ru.dankoy.telegrambot.core.domain.subscription.tag.TagSubscription;

/**
 * @deprecated since spring-boot 4.0.0 in favor {@link ChannelSubscriptionServiceHttpClient}
 */
@Deprecated(since = "2025-01-04", forRemoval = true)
@FeignClient(name = "subscriptions-holder")
public interface SubscriptionsHolderFeign {

  // subscriptions

  /**
   * @deprecated for topics support via messageThreadId
   */
  @Deprecated(since = "2024-04-05", forRemoval = true)
  @GetMapping(
      path = "/api/v1/community_subscriptions",
      params = {"telegramChatId"})
  List<CommunitySubscription> getAllSubscriptionsByChatId(
      @RequestParam("telegramChatId") long telegramChatId);

  /**
   * @deprecated chat is in separate microservice and db
   */
  @Deprecated(since = "2025-02-28", forRemoval = false)
  @GetMapping(
      path = "/api/v1/community_subscriptions",
      params = {"telegramChatId", "messageThreadId"})
  List<CommunitySubscription> getAllSubscriptionsByChatIdAndMessageThreadId(
      @RequestParam("telegramChatId") long telegramChatId,
      @RequestParam("messageThreadId") Integer messageThreadId);

  @GetMapping(
      path = "/api/v1/community_subscriptions",
      params = {"chatUuid"})
  List<CommunitySubscription> getAllCommunitySubscriptionsByChatUuid(
      @RequestParam("chatUuid") UUID chatUuid);

  @PostMapping(path = "/api/v1/community_subscriptions")
  CommunitySubscription subscribe(@RequestBody CommunitySubscription communitySubscription);

  @DeleteMapping(path = "/api/v1/community_subscriptions")
  void unsubscribe(@RequestBody CommunitySubscription communitySubscription);

  // communities

  @GetMapping(path = "/api/v1/communities/{name}")
  Community getCommunityByName(@PathVariable("name") String communityName);

  // tag subscriptions

  /**
   * @deprecated for topics support via messageThreadId
   */
  @Deprecated(since = "2024-04-05", forRemoval = true)
  @GetMapping(
      path = "/api/v1/tag_subscriptions",
      params = {"telegramChatId"})
  List<TagSubscription> getAllTagSubscriptionsByChatId(
      @RequestParam("telegramChatId") long telegramChatId);

  /**
   * @deprecated chat is in separate microservice and db
   */
  @Deprecated(since = "2025-02-28", forRemoval = false)
  @GetMapping(
      path = "/api/v1/tag_subscriptions",
      params = {"telegramChatId", "messageThreadId"})
  List<TagSubscription> getAllTagSubscriptionsByChatIdAndMessageThreadId(
      @RequestParam("telegramChatId") long telegramChatId,
      @RequestParam("messageThreadId") Integer messageThreadId);

  @GetMapping(
      path = "/api/v1/tag_subscriptions",
      params = {"chatUuid"})
  List<TagSubscription> getAllTagSubscriptionsByChatUuid(@RequestParam("chatUuid") UUID chatUuid);

  @PostMapping(path = "/api/v1/tag_subscriptions")
  TagSubscription subscribeByTag(@RequestBody TagSubscription tagSubscription);

  @DeleteMapping(path = "/api/v1/tag_subscriptions")
  void unsubscribeByTag(@RequestBody TagSubscription tagSubscription);

  // chats

  /**
   * @deprecated for topics support via messageThreadId
   */
  @Deprecated(since = "2024-04-05", forRemoval = true)
  @GetMapping(
      path = "/api/v1/telegram_chat/{chatId}",
      params = {"chatId"})
  Chat getChatById(@PathVariable("chatId") long chatId);

  /**
   * @deprecated chat is in separate microservice and db
   */
  @Deprecated(since = "2025-02-28", forRemoval = false)
  @GetMapping(
      path = "/api/v1/telegram_chat",
      params = {"chatId", "messageThreadId"})
  Chat getChatByIdAndMessageThreadId(
      @RequestParam("chatId") long chatId,
      @RequestParam("messageThreadId") Integer messageThreadId);

  /**
   * @deprecated chat is in separate microservice and db
   */
  @Deprecated(since = "2025-02-28", forRemoval = false)
  @PostMapping(path = "/api/v1/telegram_chat")
  Chat createChat(@RequestBody Chat chat);

  /**
   * @deprecated chat is in separate microservice and db
   */
  @Deprecated(since = "2025-02-28", forRemoval = false)
  @PutMapping(path = "/api/v1/telegram_chat/{id}")
  Chat updateChat(@PathVariable("id") long id, @RequestBody Chat chat);

  // communities
  @GetMapping(path = "/api/v1/communities")
  List<Community> getAllCommunities();

  // tags
  @GetMapping(
      path = "/api/v1/tags",
      params = {"title"})
  Tag getTagByTitle(@RequestParam("title") String title);

  @PostMapping(path = "/api/v1/tags")
  Tag createTag(@RequestBody Tag tag);

  // orders
  @GetMapping(path = "/api/v1/orders")
  List<Order> getAllOrders();

  @GetMapping(
      path = "/api/v1/orders",
      params = {"subscriptionType"})
  List<Order> getOrdersByType(@RequestParam String subscriptionType);

  @GetMapping(
      path = "/api/v1/orders",
      params = {"value", "subscriptionType"})
  Order getOrderByValueAndType(@RequestParam String value, @RequestParam String subscriptionType);

  // channels

  @GetMapping(
      path = "/api/v1/channels",
      params = {"permalink"})
  Channel getChannelByPermalink(@RequestParam("permalink") String permalink);

  @PostMapping(path = "/api/v1/channels")
  Channel createChannel(@RequestBody Channel channel);

  // channel subscriptions

  /**
   * @deprecated for topics support via messageThreadId
   */
  @Deprecated(since = "2024-04-05", forRemoval = true)
  @GetMapping(
      path = "/api/v1/channel_subscriptions",
      params = {"telegramChatId"})
  List<ChannelSubscription> getAllChannelSubscriptionsByChatId(
      @RequestParam("telegramChatId") long telegramChatId);

  /**
   * @deprecated chat is in separate microservice and db
   */
  @Deprecated(since = "2025-02-28", forRemoval = false)
  @GetMapping(
      path = "/api/v1/channel_subscriptions",
      params = {"telegramChatId", "messageThreadId"})
  List<ChannelSubscription> getAllChannelSubscriptionsByChatIdAndMessageThreadId(
      @RequestParam("telegramChatId") long telegramChatId,
      @RequestParam("messageThreadId") Integer messageThreadId);

  @GetMapping(
      path = "/api/v1/channel_subscriptions",
      params = {"chatUuid"})
  List<ChannelSubscription> getAllChannelSubscriptionsByChatUuid(
      @RequestParam("chatUuid") UUID chatUuid);

  @PostMapping(path = "/api/v1/channel_subscriptions")
  ChannelSubscription subscribeByChannel(@RequestBody ChannelSubscription channelSubscription);

  @DeleteMapping(path = "/api/v1/channel_subscriptions")
  void unsubscribeByChannel(@RequestBody ChannelSubscription channelSubscription);
}
