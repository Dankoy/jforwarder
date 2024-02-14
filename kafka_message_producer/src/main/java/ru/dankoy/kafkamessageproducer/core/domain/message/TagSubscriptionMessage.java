package ru.dankoy.kafkamessageproducer.core.domain.message;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Order;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Scope;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Type;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.tagsubscription.Tag;

@Getter
@SuperBuilder
@ToString(callSuper = true)
public final class TagSubscriptionMessage extends CoubMessage {
    private Tag tag;

    private Order order;

    private Scope scope;

    private Type type;
}
