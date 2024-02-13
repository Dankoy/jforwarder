package ru.dankoy.telegrambot.core.service.subscription;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.telegrambot.core.domain.Order;
import ru.dankoy.telegrambot.core.domain.Scope;
import ru.dankoy.telegrambot.core.domain.SubscriptionType;
import ru.dankoy.telegrambot.core.domain.Type;
import ru.dankoy.telegrambot.core.domain.channel.Channel;
import ru.dankoy.telegrambot.core.domain.channel.ChannelSubscription;
import ru.dankoy.telegrambot.core.domain.subscription.Chat;
import ru.dankoy.telegrambot.core.exceptions.ExceptionObjectType;
import ru.dankoy.telegrambot.core.exceptions.NotFoundException;
import ru.dankoy.telegrambot.core.service.channel.ChannelService;
import ru.dankoy.telegrambot.core.service.coubtags.CoubSmartSearcherService;
import ru.dankoy.telegrambot.core.service.order.OrderService;

@Service
@RequiredArgsConstructor
public class ChannelSubscriptionServiceImpl implements ChannelSubscriptionService {

  private final CoubSmartSearcherService coubSmartSearcherService;

  private final ChannelService channelService;

  private final OrderService orderService;

  @Override
  public List<ChannelSubscription> getSubscriptionsByChatId(long chatId) {
    return channelService.getAllSubscriptionsByChat(chatId);
  }

  @Override
  public ChannelSubscription subscribe(
      String channelPermalink, String orderValue, String scopeName, String typeName, long chatId) {
    // 1. Find tag order

    var optionalTagOrder = orderService.findByValue(orderValue, SubscriptionType.CHANNEL);

    var order =
        optionalTagOrder.orElseThrow(
            () ->
                new NotFoundException(
                    ExceptionObjectType.ORDER,
                    orderValue,
                    String.format(
                        "Order '%s' not found. Validate order and try again", orderValue)));

    // 2. find tag in db
    var optionalChannelByPermalink = channelService.findChannelByPermalink(channelPermalink);

    if (optionalChannelByPermalink.isPresent()) {
      var channel = optionalChannelByPermalink.get();
      var channelSubscription =
          new ChannelSubscription(
              0,
              channel,
              new Chat(chatId),
              order,
              new Scope(scopeName),
              new Type(typeName),
              null,
              new ArrayList<>());

      return channelService.subscribeByChannel(channelSubscription);

    } else {

      var optionalChannelFromApi =
          coubSmartSearcherService.findByChannelPermalink(channelPermalink);

      if (optionalChannelFromApi.isPresent()) {

        var channel = optionalChannelFromApi.get();

        var created = channelService.create(channel);

        var channelSubscription =
            new ChannelSubscription(
                0,
                new Channel(created.getPermalink()),
                new Chat(chatId),
                order,
                new Scope(scopeName),
                new Type(typeName),
                null,
                new ArrayList<>());

        return channelService.subscribeByChannel(channelSubscription);

      } else {
        throw new NotFoundException(
            ExceptionObjectType.TAG,
            channelPermalink,
            String.format(
                "Channel '%s' not found. Validate tag name and try again.", channelPermalink));
      }
    }
  }

  @Override
  public void unsubscribe(
      String channelPermalink, String orderValue, String scopeName, String typeName, long chatId) {

    var channelSubscription =
        new ChannelSubscription(
            0,
            new Channel(channelPermalink),
            new Chat(chatId),
            new Order(orderValue),
            new Scope(scopeName),
            new Type(typeName),
            null,
            new ArrayList<>());

    channelService.unsubscribeByChannel(channelSubscription);
  }
}
