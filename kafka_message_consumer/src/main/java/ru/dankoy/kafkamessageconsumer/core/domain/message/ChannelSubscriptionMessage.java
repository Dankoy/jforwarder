package ru.dankoy.kafkamessageconsumer.core.domain.message;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Order;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Scope;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Type;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.channelsubscription.Channel;

@Getter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class ChannelSubscriptionMessage extends CoubMessage {
  private Channel channel;

  private Order order;

  private Scope scope;

  private Type type;
}
