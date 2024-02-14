package ru.dankoy.kafkamessageproducer.core.domain.subscription.tagsubscription;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Order;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Scope;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Subscription;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Type;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TagSubscription extends Subscription {

    private Tag tag;

    private Order order;

    private Scope scope;

    private Type type;
}
