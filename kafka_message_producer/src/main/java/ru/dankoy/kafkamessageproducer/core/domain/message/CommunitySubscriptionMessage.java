package ru.dankoy.kafkamessageproducer.core.domain.message;

import ru.dankoy.kafkamessageproducer.core.domain.coub.Coub;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Chat;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.communitysubscription.Community;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.communitysubscription.Section;

public record CommunitySubscriptionMessage(
    long id,
    Community community,
    Chat chat,
    Section section,
    Coub coub,
    String lastPermalink
) {

}
