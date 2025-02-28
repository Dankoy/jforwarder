package ru.dankoy.subscriptions_scheduler.core.service.subscriptions;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import ru.dankoy.subscriptions_scheduler.core.dto.subscriptions.SubscriptionDTO;

public interface SubscriptionsHolderService {

  List<SubscriptionDTO> getAllSubscriptionsByChatUuids(List<UUID> chatUuids, Pageable pageable);
}
