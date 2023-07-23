package ru.dankoy.kafkamessageproducer.core.domain.message;

import ru.dankoy.kafkamessageproducer.core.domain.coub.Coub;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Chat;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Community;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Section;

public record SubscriptionMessage(
    long id,
    Community community,
    Chat chat,
    Section section,
    Coub coub,
    String lastPermalink
) {

}
