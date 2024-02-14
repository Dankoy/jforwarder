package ru.dankoy.telegrambot.core.service.channel;

import feign.FeignException.NotFound;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.telegrambot.core.domain.subscription.channel.Channel;
import ru.dankoy.telegrambot.core.domain.subscription.channel.ChannelSubscription;
import ru.dankoy.telegrambot.core.feign.subscriptionsholder.SubscriptionsHolderFeign;

@RequiredArgsConstructor
@Service
public class ChannelServiceImpl implements ChannelService {

    private final SubscriptionsHolderFeign subscriptionsHolderFeign;

    @Override
    public Optional<Channel> findChannelByPermalink(String permalink) {

        try {
            return Optional.of(subscriptionsHolderFeign.getChannelByPermalink(permalink));
        } catch (NotFound e) {
            return Optional.empty();
        }
    }

    @Override
    public ChannelSubscription subscribeByChannel(ChannelSubscription channelSubscription) {

        return subscriptionsHolderFeign.subscribeByChannel(channelSubscription);
    }

    @Override
    public void unsubscribeByChannel(ChannelSubscription channelSubscription) {

        subscriptionsHolderFeign.unsubscribeByChannel(channelSubscription);
    }

    @Override
    public List<ChannelSubscription> getAllSubscriptionsByChat(long chatId) {

        return subscriptionsHolderFeign.getAllChannelSubscriptionsByChatId(chatId);
    }

    @Override
    public Channel create(Channel channel) {
        return subscriptionsHolderFeign.createChannel(channel);
    }
}
