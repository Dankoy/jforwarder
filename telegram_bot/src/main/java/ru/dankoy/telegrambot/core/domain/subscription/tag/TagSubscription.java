package ru.dankoy.telegrambot.core.domain.subscription.tag;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dankoy.telegrambot.core.domain.subscription.Order;
import ru.dankoy.telegrambot.core.domain.subscription.Scope;
import ru.dankoy.telegrambot.core.domain.subscription.Subscription;
import ru.dankoy.telegrambot.core.domain.subscription.Type;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TagSubscription extends Subscription {

  private Tag tag;

  private Order order;

  private Scope scope;

  private Type type;
}
