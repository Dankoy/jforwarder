package ru.dankoy.telegrambot.core.domain.message;


import ru.dankoy.telegrambot.core.domain.coub.Coub;
import ru.dankoy.telegrambot.core.domain.subscription.Chat;
import ru.dankoy.telegrambot.core.domain.tagsubscription.Order;
import ru.dankoy.telegrambot.core.domain.tagsubscription.Scope;
import ru.dankoy.telegrambot.core.domain.tagsubscription.Tag;
import ru.dankoy.telegrambot.core.domain.tagsubscription.Type;

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
