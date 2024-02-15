package ru.dankoy.telegrambot.core.domain.subscription.community;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dankoy.telegrambot.core.domain.subscription.Subscription;

@EqualsAndHashCode(callSuper = true)
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CommunitySubscription extends Subscription {

  private Community community;
  private Section section;
}
