package ru.dankoy.kafkamessageconsumer.core.domain.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dankoy.kafkamessageconsumer.core.domain.tagsubscription.Order;
import ru.dankoy.kafkamessageconsumer.core.domain.tagsubscription.Scope;
import ru.dankoy.kafkamessageconsumer.core.domain.tagsubscription.Tag;
import ru.dankoy.kafkamessageconsumer.core.domain.tagsubscription.Type;

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
