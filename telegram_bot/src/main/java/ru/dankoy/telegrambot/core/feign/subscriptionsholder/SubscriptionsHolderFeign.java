package ru.dankoy.telegrambot.core.feign.subscriptionsholder;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dankoy.telegrambot.core.domain.subscription.Chat;
import ru.dankoy.telegrambot.core.domain.subscription.Community;
import ru.dankoy.telegrambot.core.domain.subscription.CommunitySubscription;
import ru.dankoy.telegrambot.core.domain.tagsubscription.Order;
import ru.dankoy.telegrambot.core.domain.tagsubscription.Tag;
import ru.dankoy.telegrambot.core.domain.tagsubscription.TagSubscription;

@FeignClient(name = "subscriptions-holder")
public interface SubscriptionsHolderFeign {

  // subscriptions
  @GetMapping(
      path = "/api/v1/community_subscriptions",
      params = {"telegramChatId"})
  List<CommunitySubscription> getAllSubscriptionsByChatId(
      @RequestParam("telegramChatId") long telegramChatId);

  @PostMapping(path = "/api/v1/community_subscriptions")
  CommunitySubscription subscribe(@RequestBody CommunitySubscription communitySubscription);

  @DeleteMapping(path = "/api/v1/community_subscriptions")
  void unsubscribe(@RequestBody CommunitySubscription communitySubscription);

  // communities

  @GetMapping(path = "/api/v1/communities/{name}")
  Community getCommunityByName(@PathVariable("name") String communityName);

  // tag subscriptions
  @GetMapping(
      path = "/api/v1/tag_subscriptions",
      params = {"telegramChatId"})
  List<TagSubscription> getAllTagSubscriptionsByChatId(
      @RequestParam("telegramChatId") long telegramChatId);

  @PostMapping(path = "/api/v1/tag_subscriptions")
  TagSubscription subscribeByTag(@RequestBody TagSubscription tagSubscription);

  @DeleteMapping(path = "/api/v1/tag_subscriptions")
  void unsubscribeByTag(@RequestBody TagSubscription tagSubscription);

  // chats
  @GetMapping(
      path = "/api/v1/telegram_chat/{chatId}",
      params = {"chatId"})
  Chat getChatById(@PathVariable("chatId") long chatId);

  @PostMapping(path = "/api/v1/telegram_chat")
  Chat createChat(@RequestBody Chat chat);

  @PutMapping(path = "/api/v1/telegram_chat")
  Chat updateChat(@RequestBody Chat chat);

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

  // tag orders
  @GetMapping(path = "/api/v1/tag_orders")
  List<Order> getAllTagOrders();

  @GetMapping(
      path = "/api/v1/tag_orders",
      params = {"value"})
  Order getOrderByValue(@RequestParam String value);
}
