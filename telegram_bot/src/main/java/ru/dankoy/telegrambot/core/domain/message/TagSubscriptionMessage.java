package ru.dankoy.telegrambot.core.domain.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dankoy.telegrambot.core.domain.Order;
import ru.dankoy.telegrambot.core.domain.Scope;
import ru.dankoy.telegrambot.core.domain.tagsubscription.Tag;
import ru.dankoy.telegrambot.core.domain.Type;

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
