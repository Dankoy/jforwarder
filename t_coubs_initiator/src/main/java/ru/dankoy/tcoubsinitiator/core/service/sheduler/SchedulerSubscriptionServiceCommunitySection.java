package ru.dankoy.tcoubsinitiator.core.service.sheduler;

// по заданному времени лезет в апи куба, проверяет свои подписки и создает сообщения

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.Coub;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.communitysubscription.CommunitySubscription;
import ru.dankoy.tcoubsinitiator.core.service.coubfinder.CoubFinderService;
import ru.dankoy.tcoubsinitiator.core.service.filter.FilterByRegistryService;
import ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice.MessageProducerCommunitySubscriptionService;
import ru.dankoy.tcoubsinitiator.core.service.subscription.SubscriptionService;

/**
 * @deprecated in favor for @{link SchedulerCommunitySubscriptionService.class}
 */
@Deprecated(since = "2024-10-24", forRemoval = true)
@Slf4j
// @Service
@RequiredArgsConstructor
public class SchedulerSubscriptionServiceCommunitySection {

  private static final int FIRST_PAGE = 0;
  private static final int PAGE_SIZE = 3;

  private final SubscriptionService subscriptionService;
  private final MessageProducerCommunitySubscriptionService
      messageProducerCommunitySubscriptionService;
  private final CoubFinderService coubFinderService;
  private final FilterByRegistryService filter;

  @Scheduled(initialDelay = 30_000, fixedRate = 6_000_000) // 100 mins
  public void scheduledOperation() {

    int page = FIRST_PAGE;
    int totalPages = Integer.MAX_VALUE;

    // iterate by pages
    while (page <= totalPages) {

      var sort = Sort.by("id").ascending();
      var pageable = PageRequest.of(page, PAGE_SIZE, sort);

      Page<CommunitySubscription> communitySubscriptionsPage =
          subscriptionService.getAllSubscriptionsWithActiveChats(pageable);

      totalPages = communitySubscriptionsPage.getTotalPages() - 1;

      log.info("CommunitySubscriptions page - {}", communitySubscriptionsPage);
      log.info("CommunitySubscriptions - {}", communitySubscriptionsPage.getContent());

      // поиск кубов из апи с last_permalink
      for (var subscription : communitySubscriptionsPage) {

        log.info("Working with subscription - '{}'", subscription);

        List<Coub> coubsToSend =
            coubFinderService.findUnsentCoubsForCommunitySubscription(subscription);

        // reverse coubs
        Collections.reverse(coubsToSend);

        subscription.addCoubs(coubsToSend);
      }

      filter.filterByRegistry(communitySubscriptionsPage.getContent());

      // remove subscriptions without coubs

      var toSend =
          communitySubscriptionsPage.stream().filter(s -> !s.getCoubs().isEmpty()).toList();

      // send to message producer service

      log.info("Coubs to send for all subscriptions - {}", toSend);

      if (!toSend.isEmpty()) {
        messageProducerCommunitySubscriptionService.sendCommunitySubscriptionsData(toSend);
      }

      log.info("Page {} of {} is done", page, totalPages);
      log.info(
          "Amount of community subscriptions processed: {}",
          communitySubscriptionsPage.getContent().size());

      page++;
    }
  }
}
