package ru.dankoy.kafkamessageconsumer.core.domain.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Order;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Scope;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Type;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.tagsubscription.Tag;

@Getter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
public final class TagSubscriptionMessage extends CoubMessage {
  private Tag tag;

  private Order order;

  private Scope scope;

  private Type type;
}
