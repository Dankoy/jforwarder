package ru.dankoy.tcoubsinitiator.core.service.sheduler;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.Coub;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.tagsubscription.TagSubscription;
import ru.dankoy.tcoubsinitiator.core.service.coubfinder.CoubFinderService;
import ru.dankoy.tcoubsinitiator.core.service.filter.FilterByRegistryService;
import ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice.MessageProducerTagSubscriptionService;
import ru.dankoy.tcoubsinitiator.core.service.tagsubscription.TagSubscriptionService;

/**
 * @deprecated in favor for @{link SchedulerTagSubscriptionService.class}
 */
@Deprecated(since = "2024-10-24", forRemoval = true)
@Slf4j
// @Service
@RequiredArgsConstructor
public class SchedulerSubscriptionServiceTag {

  private static final int FIRST_PAGE = 0;
  private static final int PAGE_SIZE = 3;

  private final TagSubscriptionService tagSubscriptionService;
  private final MessageProducerTagSubscriptionService messageProducerTagSubscriptionService;
  private final CoubFinderService coubFinderService;
  private final FilterByRegistryService filter;

  @Scheduled(initialDelay = 90_000, fixedRate = 6_000_000) // 100 mins
  public void scheduledOperation() {

    int page = FIRST_PAGE;
    int totalPages = Integer.MAX_VALUE;

    // iterate by pages
    while (page <= totalPages) {

      var sort = Sort.by("id").ascending();
      var pageable = PageRequest.of(page, PAGE_SIZE, sort);

      Page<TagSubscription> tagSubscriptionsPage =
          tagSubscriptionService.getAllSubscriptionsWithActiveChats(pageable);

      totalPages = tagSubscriptionsPage.getTotalPages() - 1;

      log.info("TagSubscriptions page - {}", tagSubscriptionsPage);
      log.info("TagSubscriptions - {}", tagSubscriptionsPage.getContent());

      // поиск кубов из апи с last_permalink
      for (var subscription : tagSubscriptionsPage) {

        log.info("Working with subscription - '{}'", subscription);

        List<Coub> coubsToSend = coubFinderService.findUnsentCoubsForTagSubscription(subscription);

        // reverse coubs
        Collections.reverse(coubsToSend);

        subscription.addCoubs(coubsToSend);
      }

      filter.filterByRegistry(tagSubscriptionsPage.getContent());

      // remove subscriptions without coubs

      var toSend = tagSubscriptionsPage.stream().filter(s -> !s.getCoubs().isEmpty()).toList();

      // send to message producer service

      log.info("Coubs to send for all subscriptions - {}", toSend);

      if (!toSend.isEmpty()) {
        messageProducerTagSubscriptionService.sendTagSubscriptionsData(toSend);
      }

      log.info("Page {} of {} is done", page, totalPages);
      log.info(
          "Amount of tag subscriptions processed: {}", tagSubscriptionsPage.getContent().size());

      page++;
    }
  }
}
