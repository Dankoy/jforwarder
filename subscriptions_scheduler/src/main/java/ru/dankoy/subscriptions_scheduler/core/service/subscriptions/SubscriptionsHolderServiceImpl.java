package ru.dankoy.subscriptions_scheduler.core.service.subscriptions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptions_scheduler.core.dto.subscriptions.SubscriptionDTO;
import ru.dankoy.subscriptions_scheduler.core.feign.SubscriptionsHolderFeign;

@Service
@RequiredArgsConstructor
public class SubscriptionsHolderServiceImpl implements SubscriptionsHolderService {

  private final SubscriptionsHolderFeign subscriptionsHolderFeign;

  @Override
  public List<SubscriptionDTO> getAllSubscriptionsByChatUuids(
      List<UUID> chatUuids, Pageable pageable) {

    List<SubscriptionDTO> result = new ArrayList<>();

    int page = 0;
    Page<SubscriptionDTO> subs =
        subscriptionsHolderFeign.getSubscriptionsFiltered(chatUuids, pageable);
    var maxPage = subs.getTotalPages();

    while (page <= maxPage) {

      result.addAll(subs.getContent());
    }

    return result;
  }
}
