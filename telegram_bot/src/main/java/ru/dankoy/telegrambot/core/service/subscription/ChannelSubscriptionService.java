package ru.dankoy.telegrambot.core.service.subscription;

import java.util.List;
import ru.dankoy.telegrambot.core.domain.subscription.channel.ChannelSubscription;

public interface ChannelSubscriptionService {

  List<ChannelSubscription> getSubscriptionsByChatId(long chatId);

  List<ChannelSubscription> getSubscriptionsByChatIdAndMessageThreadId(
      long chatId, Integer messageThreadId);

  ChannelSubscription subscribe(
      String channelPermalink,
      String orderValue,
      String scopeName,
      String typeName,
      long chatId,
      Integer messageThreadId);

  void unsubscribe(
      String channelPermalink,
      String orderValue,
      String scopeName,
      String typeName,
      long chatId,
      Integer messageThreadId);
}
