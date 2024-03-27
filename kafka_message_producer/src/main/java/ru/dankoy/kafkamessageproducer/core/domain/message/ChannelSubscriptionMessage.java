package ru.dankoy.kafkamessageproducer.core.domain.message;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Order;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Scope;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Type;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.channelsubscription.Channel;

@Getter
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public final class ChannelSubscriptionMessage extends CoubMessage {
  private Channel channel;

  private Order order;

  private Scope scope;

  private Type type;
}
