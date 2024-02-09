package ru.dankoy.kafkamessageproducer.core.domain.message;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.tagsubscription.Order;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.tagsubscription.Scope;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.tagsubscription.Tag;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.tagsubscription.Type;

@Getter
@SuperBuilder
@ToString(callSuper = true)
public final class TagSubscriptionMessage extends CoubMessage {
  private Tag tag;

  private Order order;

  private Scope scope;

  private Type type;
}
