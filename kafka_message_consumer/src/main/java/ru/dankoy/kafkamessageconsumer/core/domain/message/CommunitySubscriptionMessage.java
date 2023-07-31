package ru.dankoy.kafkamessageconsumer.core.domain.message;

import ru.dankoy.kafkamessageconsumer.core.domain.coub.Coub;
import ru.dankoy.kafkamessageconsumer.core.domain.communitysubscription.Chat;
import ru.dankoy.kafkamessageconsumer.core.domain.communitysubscription.Community;
import ru.dankoy.kafkamessageconsumer.core.domain.communitysubscription.Section;

public record CommunitySubscriptionMessage(
    long id,
    Community community,
    Chat chat,
    Section section,
    Coub coub,
    String lastPermalink
) {

}
