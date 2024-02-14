package ru.dankoy.telegrambot.core.service.subscription;

import java.util.List;
import ru.dankoy.telegrambot.core.domain.subscription.channel.ChannelSubscription;

public interface ChannelSubscriptionService {

    List<ChannelSubscription> getSubscriptionsByChatId(long chatId);

    ChannelSubscription subscribe(
            String channelPermalink,
            String orderValue,
            String scopeName,
            String typeName,
            long chatId);

    void unsubscribe(
            String channelPermalink,
            String orderValue,
            String scopeName,
            String typeName,
            long chatId);
}
