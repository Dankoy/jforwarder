package ru.dankoy.kafkamessageconsumer.core.domain.message;

import ru.dankoy.kafkamessageconsumer.core.domain.coub.Coub;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Chat;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Community;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Section;

public record SubscriptionMessage(
    long id,
    Community community,
    Chat chat,
    Section section,
    Coub coub,
    String lastPermalink
) {

}
