package ru.dankoy.tcoubsinitiator.core.service.tagsubscription;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.tagsubscription.TagSubscription;
import ru.dankoy.tcoubsinitiator.core.feign.subscription.SubscriptionFeign;

/**
 * @deprecated since spring-boot 4.0.0 in favor {@link ChannelSubscriptionServiceHttpClient}
 */
@Deprecated(since = "2025-01-04", forRemoval = true)
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

  @Override
  public Page<TagSubscription> getAllSubscriptionsByChatUuid(
      List<UUID> chatUuids, Pageable pageable) {
    return subscriptionFeign.getTagSubscriptionByChatUuids(chatUuids, pageable);
  }
}
