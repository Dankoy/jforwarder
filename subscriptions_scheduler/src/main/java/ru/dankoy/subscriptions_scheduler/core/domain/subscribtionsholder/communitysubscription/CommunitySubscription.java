package ru.dankoy.subscriptions_scheduler.core.domain.subscribtionsholder.communitysubscription;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptions_scheduler.core.domain.subscribtionsholder.subscription.Subscription;

@EqualsAndHashCode(callSuper = true)
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CommunitySubscription extends Subscription {

  private Community community;
  private Section section;
}
