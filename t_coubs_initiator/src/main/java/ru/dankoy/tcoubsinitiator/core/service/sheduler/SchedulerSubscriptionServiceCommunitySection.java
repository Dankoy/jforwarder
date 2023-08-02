package ru.dankoy.tcoubsinitiator.core.service.sheduler;

// по заданному времени лезет в апи куба, проверяет свои подписки и создает сообщения


import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.Coub;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.communitysubscription.CommunitySubscription;
import ru.dankoy.tcoubsinitiator.core.service.coubfinder.CoubFinderService;
import ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice.MessageProducerCommunitySubscriptionService;
import ru.dankoy.tcoubsinitiator.core.service.subscription.SubscriptionService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerSubscriptionServiceCommunitySection {

  private final SubscriptionService subscriptionService;
  private final MessageProducerCommunitySubscriptionService messageProducerCommunitySubscriptionService;
  private final CoubFinderService coubFinderService;

  @Scheduled(initialDelay = 30_000, fixedRate = 6_000_000) // 100 mins
  public void scheduledOperation() {

    List<CommunitySubscription> communitySubscriptions = subscriptionService.getAllSubscriptionsWithActiveChats();
    log.info("Subscriptions - {}", communitySubscriptions);

    // поиск кубов из апи с last_permalink
    for (var subscription : communitySubscriptions) {

      log.info("Working with subscription - '{}'", subscription);

      List<Coub> coubsToSend = coubFinderService.findUnsentCoubsForCommunitySubscription(
          subscription);

      // reverse coubs
      Collections.reverse(coubsToSend);

      subscription.addCoubs(coubsToSend);

    }

    // remove subscriptions without coubs

    var toSend = communitySubscriptions.stream()
        .filter(s -> !s.getCoubs().isEmpty())
        .toList();

    //send to message producer service

    log.info("Coubs to send for all subscriptions - {}", toSend);

    if (!toSend.isEmpty()) {
      messageProducerCommunitySubscriptionService.sendCommunitySubscriptionsData(toSend);
    }

  }

}
