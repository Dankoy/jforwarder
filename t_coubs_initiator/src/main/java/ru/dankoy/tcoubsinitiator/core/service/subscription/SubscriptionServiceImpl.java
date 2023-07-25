package ru.dankoy.tcoubsinitiator.core.service.subscription;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.subscription.Subscription;
import ru.dankoy.tcoubsinitiator.core.feign.subscription.SubscriptionFeign;


@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

  private final SubscriptionFeign subscriptionFeign;

  @Override
  public List<Subscription> getAllSubscriptions() {
    return subscriptionFeign.getAllSubscriptions();
  }

  @Override
  public List<Subscription> getAllSubscriptionsWithActiveChats() {
    return subscriptionFeign.getAllSubscriptionsWithActiveChats(true);
  }
}
