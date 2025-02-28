package ru.dankoy.subscriptions_scheduler.core.service.subscriptions;

import java.util.List;
import org.springframework.data.domain.Pageable;
import ru.dankoy.subscriptions_scheduler.core.dto.subscriptions.SubscriptionDTO;

public interface SubscriptionsHolderService {

  List<SubscriptionDTO> getAllSubscriptionsWithFilter(String search, Pageable pageable);
}
