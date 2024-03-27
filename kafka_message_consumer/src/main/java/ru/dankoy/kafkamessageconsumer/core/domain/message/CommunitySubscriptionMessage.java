package ru.dankoy.kafkamessageconsumer.core.domain.message;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.communitysubscription.Community;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.communitysubscription.Section;

@Getter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class CommunitySubscriptionMessage extends CoubMessage {
  private Community community;
  private Section section;
}
