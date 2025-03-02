package ru.dankoy.subscriptions_scheduler.core.service.subscriptions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptions_scheduler.core.dto.subscriptions.SubscriptionDTO;
import ru.dankoy.subscriptions_scheduler.core.feign.SubscriptionsHolderFeign;

@Service
@RequiredArgsConstructor
public class SubscriptionsHolderServiceImpl implements SubscriptionsHolderService {

  private final SubscriptionsHolderFeign subscriptionsHolderFeign;

  @Override
  public List<SubscriptionDTO> getAllSubscriptionsByChatUuids(List<UUID> chatUuids) {

    List<SubscriptionDTO> result = new ArrayList<>();

    int page = 0;
    int maxPage = Integer.MAX_VALUE;
    int pageSize = 10;

    do {

      Pageable pageable = PageRequest.of(page, pageSize);

      Page<SubscriptionDTO> subs =
          subscriptionsHolderFeign.getSubscriptionsFiltered(chatUuids, pageable);
      maxPage = subs.getTotalPages() - 1;
      result.addAll(subs.getContent());
      page++;
    } while (page <= maxPage);

    return result;
  }
}
