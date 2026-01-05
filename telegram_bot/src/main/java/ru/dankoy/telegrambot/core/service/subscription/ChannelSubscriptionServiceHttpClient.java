package ru.dankoy.telegrambot.core.service.subscription;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.dankoy.telegrambot.core.domain.Chat;
import ru.dankoy.telegrambot.core.domain.subscription.Order;
import ru.dankoy.telegrambot.core.domain.subscription.Scope;
import ru.dankoy.telegrambot.core.domain.subscription.SubscriptionType;
import ru.dankoy.telegrambot.core.domain.subscription.Type;
import ru.dankoy.telegrambot.core.domain.subscription.channel.Channel;
import ru.dankoy.telegrambot.core.domain.subscription.channel.ChannelSubscription;
import ru.dankoy.telegrambot.core.exceptions.ExceptionObjectType;
import ru.dankoy.telegrambot.core.exceptions.NotFoundException;
import ru.dankoy.telegrambot.core.httpservice.subscriptionsholder.SubscriptionsHolderChannelSubHttpService;
import ru.dankoy.telegrambot.core.service.channel.ChannelService;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatService;
import ru.dankoy.telegrambot.core.service.coubtags.CoubSmartSearcherService;
import ru.dankoy.telegrambot.core.service.order.OrderService;

@Service
@RequiredArgsConstructor
public class ChannelSubscriptionServiceHttpClient implements ChannelSubscriptionService {

  @Qualifier("coubSmartSearcherServiceHttpClient")
  private final CoubSmartSearcherService coubSmartSearcherService;

  @Qualifier("channelServiceHttpClient")
  private final ChannelService channelService;

  @Qualifier("orderServiceHttpClient")
  private final OrderService orderService;

  @Qualifier("telegramChatServiceHttpClient")
  private final TelegramChatService telegramChatService;

  private final SubscriptionsHolderChannelSubHttpService channelSubHttpService;

  /**
   * @deprecated for topics support via messageThreadId
   */
  @Deprecated(since = "2024-04-05", forRemoval = true)
  @Override
  public List<ChannelSubscription> getSubscriptionsByChatId(long chatId) {
    return channelService.getAllSubscriptionsByChat(chatId);
  }

  /**
   * @deprecated chat is in separate microservice and db
   */
  @Deprecated(since = "2025-02-28", forRemoval = false)
  @Override
  public List<ChannelSubscription> getSubscriptionsByChatIdAndMessageThreadId(
      long chatId, Integer messageThreadId) {
    return channelService.getAllSubscriptionsByChatIdAndMessageThreadId(chatId, messageThreadId);
  }

  @Override
  public List<ChannelSubscription> getSubsByChatIdAndMessageThreadId(
      long chatId, Integer messageThreadId) {

    var chat = telegramChatService.getChatByIdAndMessageThreadId(chatId, messageThreadId);

    return channelSubHttpService.getAllChannelSubscriptionsByChatUuid(chat.getId());
  }

  @Override
  public ChannelSubscription subscribe(
      String channelPermalink,
      String orderValue,
      String scopeName,
      String typeName,
      long chatId,
      Integer messageThreadId) {
    // 1. Find tag order

    var optionalTagOrder = orderService.findByValue(orderValue, SubscriptionType.CHANNEL);

    var order =
        optionalTagOrder.orElseThrow(
            () ->
                new NotFoundException(
                    ExceptionObjectType.ORDER,
                    orderValue,
                    String.format(
                        "Order '%s' not found. Validate order and try" + " again", orderValue)));

    order.setSubscriptionType(SubscriptionType.CHANNEL);

    // 2. find tag in db
    var optionalChannelByPermalink = channelService.findChannelByPermalink(channelPermalink);

    // get chat from separate microservice
    // chat is present in db in that point.
    var chat = telegramChatService.getChatByIdAndMessageThreadId(chatId, messageThreadId);

    if (optionalChannelByPermalink.isPresent()) {

      var jpaChat =
          Chat.builder()
              .id(0)
              .chatId(chatId)
              .messageThreadId(messageThreadId)
              .active(true)
              .firstName(chat.getFirstName())
              .lastName(chat.getLastName())
              .username(chat.getUsername())
              .type(chat.getType())
              .title(chat.getTitle())
              .build();

      var channel = optionalChannelByPermalink.get();
      var channelSubscription =
          (ChannelSubscription)
              ChannelSubscription.builder()
                  .id(0)
                  .channel(channel)
                  .chat(jpaChat)
                  .chatUuid(chat.getId())
                  .order(order)
                  .scope(new Scope(scopeName))
                  .type(new Type(typeName))
                  .build();

      return channelService.subscribeByChannel(channelSubscription);

    } else {

      var optionalChannelFromApi =
          coubSmartSearcherService.findByChannelPermalink(channelPermalink);

      if (optionalChannelFromApi.isPresent()) {

        var channel = optionalChannelFromApi.get();

        var created = channelService.create(channel);

        var channelSubscription =
            (ChannelSubscription)
                ChannelSubscription.builder()
                    .id(0)
                    .channel(new Channel(created.getPermalink()))
                    .chat(new Chat(chatId, messageThreadId))
                    .chatUuid(chat.getId())
                    .order(order)
                    .scope(new Scope(scopeName))
                    .type(new Type(typeName))
                    .build();

        return channelService.subscribeByChannel(channelSubscription);

      } else {
        throw new NotFoundException(
            ExceptionObjectType.CHANNEL,
            channelPermalink,
            String.format(
                "Channel '%s' not found. Validate tag name and try again.", channelPermalink));
      }
    }
  }

  @Override
  public void unsubscribe(
      String channelPermalink,
      String orderValue,
      String scopeName,
      String typeName,
      long chatId,
      Integer messageThreadId) {

    var order = new Order(orderValue);
    order.setSubscriptionType(SubscriptionType.CHANNEL);

    // get chat from separate microservice
    var chat = telegramChatService.getChatByIdAndMessageThreadId(chatId, messageThreadId);

    var channelSubscription =
        (ChannelSubscription)
            ChannelSubscription.builder()
                .id(0)
                .channel(new Channel(channelPermalink))
                .chat(new Chat(chatId, messageThreadId))
                .chatUuid(chat.getId())
                .order(order)
                .scope(new Scope(scopeName))
                .type(new Type(typeName))
                .build();

    channelService.unsubscribeByChannel(channelSubscription);
  }
}
