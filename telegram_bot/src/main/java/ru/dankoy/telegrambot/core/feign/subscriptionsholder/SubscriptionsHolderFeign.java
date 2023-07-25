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
import ru.dankoy.telegrambot.core.domain.subscription.Subscription;

@FeignClient(name = "subscriptions-holder")
public interface SubscriptionsHolderFeign {


  //subscriptions
  @GetMapping(path = "/api/v1/subscriptions", params = {"telegramChatId"})
  List<Subscription> getAllSubscriptionsByChatId(
      @RequestParam("telegramChatId") long telegramChatId);

  @PostMapping(path = "/api/v1/subscriptions")
  Subscription subscribe(@RequestBody Subscription subscription);

  @DeleteMapping(path = "/api/v1/subscriptions")
  void unsubscribe(@RequestBody Subscription subscription);

  // chats
  @GetMapping(path = "/api/v1/telegram_chat/{chatId}", params = {"chatId"})
  Chat getChatById(@PathVariable("chatId") long chatId);

  @PostMapping(path = "/api/v1/telegram_chat")
  Chat createChat(@RequestBody Chat chat);

  @PutMapping(path = "/api/v1/telegram_chat")
  Chat updateChat(@RequestBody Chat chat);

  //communities
  @GetMapping(path = "/api/v1/communities")
  List<Community> getAllCommunities();

}
