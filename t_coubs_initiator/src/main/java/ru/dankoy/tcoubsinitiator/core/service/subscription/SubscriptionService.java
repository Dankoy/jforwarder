package ru.dankoy.tcoubsinitiator.core.service.subscription;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.communitysubscription.CommunitySubscription;

public interface SubscriptionService {

  Page<CommunitySubscription> getAllSubscriptions(Pageable pageable);

  Page<CommunitySubscription> getAllSubscriptionsWithActiveChats(Pageable pageable);

  Page<CommunitySubscription> getAllSubscriptionsByChatUuid(
      List<UUID> chatUuids, Pageable pageable);
}
