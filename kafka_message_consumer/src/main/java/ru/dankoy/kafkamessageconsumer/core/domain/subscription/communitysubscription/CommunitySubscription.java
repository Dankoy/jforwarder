package ru.dankoy.kafkamessageconsumer.core.domain.subscription.communitysubscription;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Subscription;

@EqualsAndHashCode(callSuper = true)
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommunitySubscription extends Subscription {

  private Community community;
  private Section section;
}
