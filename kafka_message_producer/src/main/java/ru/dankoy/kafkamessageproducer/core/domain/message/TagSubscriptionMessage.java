package ru.dankoy.kafkamessageproducer.core.domain.message;

import ru.dankoy.kafkamessageproducer.core.domain.coub.Coub;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Chat;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.tagsubscription.Order;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.tagsubscription.Scope;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.tagsubscription.Tag;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.tagsubscription.Type;

public record TagSubscriptionMessage(
    long id,
    Tag tag,

    Chat chat,

    Order order,

    Scope scope,

    Type type,
    Coub coub,

    String lastPermalink
) {

}
