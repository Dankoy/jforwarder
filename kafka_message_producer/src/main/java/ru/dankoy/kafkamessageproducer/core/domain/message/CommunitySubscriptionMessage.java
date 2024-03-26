package ru.dankoy.kafkamessageproducer.core.domain.message;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.communitysubscription.Community;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.communitysubscription.Section;

@Getter
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public final class CommunitySubscriptionMessage extends CoubMessage {
  private Community community;
  private Section section;
}
