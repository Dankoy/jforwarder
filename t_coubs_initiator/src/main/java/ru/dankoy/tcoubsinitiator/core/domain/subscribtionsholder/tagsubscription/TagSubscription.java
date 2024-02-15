package ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.tagsubscription;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.Order;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.Scope;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.Type;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.subscription.Subscription;

@EqualsAndHashCode(callSuper = true)
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class TagSubscription extends Subscription {

  private Tag tag;

  private Order order;

  private Scope scope;

  private Type type;
}
