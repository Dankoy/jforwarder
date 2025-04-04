package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.subscription.channel;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.ChannelSub;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.channel.Channel;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.order.OrderMaker;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.scope.ScopeMaker;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.type.TypeMaker;

public interface ChannelSubMaker extends ScopeMaker, TypeMaker, OrderMaker {

  default List<ChannelSub> makeChannelSubs(Channel channel, Chat chat) {

    var value = "most_recent";
    var orderType = "channel";

    var lastPermalink = "lp";

    var scope = makeCorrectScope(); // from migration
    var type = makeCorrectType(); // from migration
    var order = findCorrectByValueAndSubscriptionType(value, orderType);

    ChannelSub channelSub =
        ChannelSub.builder()
            .id(0)
            .channel(channel)
            .scope(scope)
            .type(type)
            .order(order)
            .chat(chat)
            .chatUuid(UUID.randomUUID())
            .lastPermalink(lastPermalink)
            .build();

    return Stream.of(channelSub).toList();
  }
}
