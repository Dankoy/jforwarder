package ru.dankoy.tcoubsinitiator.core.service.tagsubscription;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.tagsubscription.TagSubscription;
import ru.dankoy.tcoubsinitiator.core.httpservice.subscription.SubscriptionHttpService;

@Primary
@Service
@RequiredArgsConstructor
public class TagSubscriptionServiceHttpClient implements TagSubscriptionService {

  private final SubscriptionHttpService subscriptionHttpService;

  @Override
  public List<TagSubscription> getAllSubscriptions(String tag) {
    return subscriptionHttpService.getSubscriptionsByTagTitle(tag);
  }

  @Override
  public Page<TagSubscription> getAllSubscriptionsWithActiveChats(Pageable pageable) {
    return subscriptionHttpService.getAllTagSubscriptionsWithActiveChats(true, pageable);
  }

  @Override
  public Page<TagSubscription> getAllSubscriptionsByChatUuid(
      List<UUID> chatUuids, Pageable pageable) {
    return subscriptionHttpService.getTagSubscriptionByChatUuids(chatUuids, pageable);
  }
}
