package ru.dankoy.telegrambot.core.domain.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dankoy.telegrambot.core.domain.subscription.Order;
import ru.dankoy.telegrambot.core.domain.subscription.Scope;
import ru.dankoy.telegrambot.core.domain.subscription.Type;
import ru.dankoy.telegrambot.core.domain.subscription.tag.Tag;

@Getter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
public final class TagSubscriptionMessage extends CoubMessage {
    private Tag tag;

    private Order order;

    private Scope scope;

    private Type type;
}
