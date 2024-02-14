package ru.dankoy.telegrambot.core.domain.subscription.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.telegrambot.core.domain.subscription.Order;
import ru.dankoy.telegrambot.core.domain.subscription.Scope;
import ru.dankoy.telegrambot.core.domain.subscription.Subscription;
import ru.dankoy.telegrambot.core.domain.subscription.Type;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChannelSubscription extends Subscription {

  private Channel channel;

  private Order order;

  private Scope scope;

  private Type type;
}
