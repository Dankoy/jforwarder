package ru.dankoy.kafkamessageconsumer.core.domain.message;


import ru.dankoy.kafkamessageconsumer.core.domain.communitysubscription.Chat;
import ru.dankoy.kafkamessageconsumer.core.domain.coub.Coub;
import ru.dankoy.kafkamessageconsumer.core.domain.tagsubscription.Order;
import ru.dankoy.kafkamessageconsumer.core.domain.tagsubscription.Scope;
import ru.dankoy.kafkamessageconsumer.core.domain.tagsubscription.Tag;
import ru.dankoy.kafkamessageconsumer.core.domain.tagsubscription.Type;

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
