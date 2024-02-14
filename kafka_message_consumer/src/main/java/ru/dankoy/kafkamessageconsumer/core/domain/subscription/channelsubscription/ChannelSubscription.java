package ru.dankoy.kafkamessageconsumer.core.domain.subscription.channelsubscription;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Order;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Scope;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Subscription;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Type;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ChannelSubscription extends Subscription {

  private Channel channel;

  private Order order;

  private Scope scope;

  private Type type;
}
