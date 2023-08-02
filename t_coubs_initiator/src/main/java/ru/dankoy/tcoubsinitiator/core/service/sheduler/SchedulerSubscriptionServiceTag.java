package ru.dankoy.tcoubsinitiator.core.service.sheduler;


import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.Coub;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.tagsubscription.TagSubscription;
import ru.dankoy.tcoubsinitiator.core.service.coubfinder.CoubFinderService;
import ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice.MessageProducerTagSubscriptionService;
import ru.dankoy.tcoubsinitiator.core.service.tagsubscription.TagSubscriptionService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerSubscriptionServiceTag {

  private final TagSubscriptionService tagSubscriptionService;
  private final MessageProducerTagSubscriptionService messageProducerTagSubscriptionService;
  private final CoubFinderService coubFinderService;

  @Scheduled(initialDelay = 60_000, fixedRate = 6_000_000) // 100 mins
  public void scheduledOperation() {

    List<TagSubscription> tagSubscriptions = tagSubscriptionService.getAllSubscriptionsWithActiveChats();
    log.info("Subscriptions - {}", tagSubscriptions);

    // поиск кубов из апи с last_permalink
    for (var subscription : tagSubscriptions) {

      log.info("Working with subscription - '{}'", subscription);

      List<Coub> coubsToSend = coubFinderService.findUnsentCoubsForTagSubscription(subscription);

      // reverse coubs
      Collections.reverse(coubsToSend);

      subscription.addCoubs(coubsToSend);

    }

    // remove subscriptions without coubs

    var toSend = tagSubscriptions.stream()
        .filter(s -> !s.getCoubs().isEmpty())
        .toList();

    //send to message producer service

    log.info("Coubs to send for all subscriptions - {}", toSend);

    if (!toSend.isEmpty()) {
      messageProducerTagSubscriptionService.sendTagSubscriptionsData(toSend);
    }

  }

}
