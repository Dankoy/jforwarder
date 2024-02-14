package ru.dankoy.telegrambot.core.service.subscription;

import java.util.List;
import lombok.RequiredArgsConstructor;
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
            String channelPermalink,
            String orderValue,
            String scopeName,
            String typeName,
            long chatId) {
        // 1. Find tag order

        var optionalTagOrder = orderService.findByValue(orderValue, SubscriptionType.CHANNEL);

        var order =
                optionalTagOrder.orElseThrow(
                        () ->
                                new NotFoundException(
                                        ExceptionObjectType.ORDER,
                                        orderValue,
                                        String.format(
                                                "Order '%s' not found. Validate order and try"
                                                        + " again",
                                                orderValue)));

        order.setSubscriptionType(SubscriptionType.CHANNEL);

        // 2. find tag in db
        var optionalChannelByPermalink = channelService.findChannelByPermalink(channelPermalink);

        if (optionalChannelByPermalink.isPresent()) {
            var channel = optionalChannelByPermalink.get();
            var channelSubscription =
                    (ChannelSubscription)
                            ChannelSubscription.builder()
                                    .id(0)
                                    .channel(channel)
                                    .chat(new Chat(chatId))
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
                                        .chat(new Chat(chatId))
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
                                "Channel '%s' not found. Validate tag name and try again.",
                                channelPermalink));
            }
        }
    }

    @Override
    public void unsubscribe(
            String channelPermalink,
            String orderValue,
            String scopeName,
            String typeName,
            long chatId) {

        var channelSubscription =
                (ChannelSubscription)
                        ChannelSubscription.builder()
                                .id(0)
                                .channel(new Channel(channelPermalink))
                                .chat(new Chat(chatId))
                                .order(new Order(orderValue))
                                .scope(new Scope(scopeName))
                                .type(new Type(typeName))
                                .build();

        channelService.unsubscribeByChannel(channelSubscription);
    }
}
