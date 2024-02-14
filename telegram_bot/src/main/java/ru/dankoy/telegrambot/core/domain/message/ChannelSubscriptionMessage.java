package ru.dankoy.telegrambot.core.domain.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dankoy.telegrambot.core.domain.subscription.Order;
import ru.dankoy.telegrambot.core.domain.subscription.Scope;
import ru.dankoy.telegrambot.core.domain.subscription.Type;
import ru.dankoy.telegrambot.core.domain.subscription.channel.Channel;

@Getter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
public final class ChannelSubscriptionMessage extends CoubMessage {
    private Channel channel;

    private Order order;

    private Scope scope;

    private Type type;
}
