package ru.dankoy.kafkamessageconsumer.core.domain.subscription.tagsubscription;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Order;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Scope;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Subscription;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Type;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class TagSubscription extends Subscription {

  private Tag tag;

  private Order order;

  private Scope scope;

  private Type type;
}
