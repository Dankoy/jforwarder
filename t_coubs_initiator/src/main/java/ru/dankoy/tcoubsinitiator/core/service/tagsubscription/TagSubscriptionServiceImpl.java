package ru.dankoy.tcoubsinitiator.core.service.tagsubscription;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.tagsubscription.TagSubscription;
import ru.dankoy.tcoubsinitiator.core.feign.subscription.SubscriptionFeign;


@Service
@RequiredArgsConstructor
public class TagSubscriptionServiceImpl implements TagSubscriptionService {

  private final SubscriptionFeign subscriptionFeign;

  @Override
  public List<TagSubscription> getAllSubscriptions(String tag) {
    return subscriptionFeign.getSubscriptionsByTagTitle(tag);
  }

  @Override
  public Page<TagSubscription> getAllSubscriptionsWithActiveChats(Pageable pageable) {
    return subscriptionFeign.getAllTagSubscriptionsWithActiveChats(true, pageable);
  }
}
