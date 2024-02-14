package ru.dankoy.telegrambot.core.service.channel;

import java.util.List;
import java.util.Optional;
import ru.dankoy.telegrambot.core.domain.subscription.channel.Channel;
import ru.dankoy.telegrambot.core.domain.subscription.channel.ChannelSubscription;

public interface ChannelService {

  Optional<Channel> findChannelByPermalink(String permalink);

  ChannelSubscription subscribeByChannel(ChannelSubscription channelSubscription);

  void unsubscribeByChannel(ChannelSubscription channelSubscription);

  List<ChannelSubscription> getAllSubscriptionsByChat(long chatId);

  Channel create(Channel tag);
}
