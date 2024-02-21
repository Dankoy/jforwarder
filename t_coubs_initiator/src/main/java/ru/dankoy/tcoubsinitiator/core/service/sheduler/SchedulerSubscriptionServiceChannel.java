package ru.dankoy.tcoubsinitiator.core.service.sheduler;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.Coub;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.channelsubscription.ChannelSubscription;
import ru.dankoy.tcoubsinitiator.core.service.channelsubscription.ChannelSubscriptionService;
import ru.dankoy.tcoubsinitiator.core.service.coubfinder.CoubFinderService;
import ru.dankoy.tcoubsinitiator.core.service.filter.FilterByRegistryService;
import ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice.MessageProducerChannelSubscriptionService;
import ru.dankoy.tcoubsinitiator.core.service.utils.Utils;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerSubscriptionServiceChannel {

  private static final int FIRST_PAGE = 0;
  private static final int PAGE_SIZE = 3;

  private final ChannelSubscriptionService channelSubscriptionService;
  private final MessageProducerChannelSubscriptionService messageProducerChannelSubscriptionService;
  private final CoubFinderService coubFinderService;
  private final FilterByRegistryService filter;

  @Scheduled(initialDelay = 120_000, fixedRate = 6_000_000) // 100 mins
  public void scheduledOperation() {

    int page = FIRST_PAGE;
    int totalPages = Integer.MAX_VALUE;

    // iterate by pages
    while (page <= totalPages) {

      var sort = Sort.by("id").ascending();
      var pageable = PageRequest.of(page, PAGE_SIZE, sort);

      Page<ChannelSubscription> allSubscriptionsWithActiveChats =
          channelSubscriptionService.getAllSubscriptionsWithActiveChats(pageable);

      totalPages = allSubscriptionsWithActiveChats.getTotalPages() - 1;

      log.info("TagSubscriptions page - {}", allSubscriptionsWithActiveChats);
      log.info("TagSubscriptions - {}", allSubscriptionsWithActiveChats.getContent());

      // поиск кубов из апи с last_permalink
      for (var subscription : allSubscriptionsWithActiveChats) {

        log.info("Working with subscription - '{}'", subscription);

        List<Coub> coubsToSend =
            coubFinderService.findUnsentCoubsForChannelSubscription(subscription);

        // reverse coubs
        Collections.reverse(coubsToSend);

        subscription.addCoubs(coubsToSend);
      }

      filter.filterByRegistry(allSubscriptionsWithActiveChats.getContent());

      // remove subscriptions without coubs

      var toSend =
          allSubscriptionsWithActiveChats.stream().filter(s -> !s.getCoubs().isEmpty()).toList();

      // send to message producer service

      log.info("Coubs to send for all subscriptions - {}", toSend);

      if (!toSend.isEmpty()) {
        messageProducerChannelSubscriptionService.sendChannelSubscriptionsData(toSend);
      }

      log.info("Page {} of {} is done", page, totalPages);
      log.info(
          "Amount of tag subscriptions processed: {}",
          allSubscriptionsWithActiveChats.getContent().size());

      page++;

      Utils.sleep(3_000);
    }
  }
}
