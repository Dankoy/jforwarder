package ru.dankoy.tcoubsinitiator.core.service.tagsubscription;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.tagsubscription.TagSubscription;

public interface TagSubscriptionService {

  List<TagSubscription> getAllSubscriptions(String tag);

  Page<TagSubscription> getAllSubscriptionsWithActiveChats(Pageable pageable);

  Page<TagSubscription> getAllSubscriptionsByChatUuid(List<UUID> chatUuids, Pageable pageable);
}
