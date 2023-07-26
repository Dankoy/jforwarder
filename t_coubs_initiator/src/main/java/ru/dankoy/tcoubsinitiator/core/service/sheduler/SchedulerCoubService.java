package ru.dankoy.tcoubsinitiator.core.service.sheduler;

// по заданному времени лезет в апи куба, проверяет свои подписки и создает сообщения


import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.Coub;
import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.CoubWrapper;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.subscription.Subscription;
import ru.dankoy.tcoubsinitiator.core.service.coub.CoubService;
import ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice.MessageProducerService;
import ru.dankoy.tcoubsinitiator.core.service.subscription.SubscriptionService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerCoubService {

  private final SubscriptionService subscriptionService;
  private final CoubService coubService;
  private final MessageProducerService messageProducerService;

  //    @Scheduled(fixedDelay = 600000)
  @Scheduled(fixedDelay = 60000)
  public void scheduledOperation() {

    List<Subscription> subscriptions = subscriptionService.getAllSubscriptionsWithActiveChats();
    log.info("Subscriptions - {}", subscriptions);

    int page = 1;
    int perPage = 25;

    try {
      for (Subscription subscription : subscriptions) {

        // this is cached, so we have to work with copy of this list
        List<Coub> coubs = getCoubsForSubscription(
            subscription.getCommunity().getName(),
            subscription.getSection().getName(),
            1,
            25);
        Thread.sleep(1000);

        subscription.addCoubs(coubs);

      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("Interrupted while trying to get coubs", e);
    }

    log.debug("Coubs for subscriptions - {}", subscriptions);

    // поиск кубов из апи с last_permalink
    for (var subscription : subscriptions) {

      log.info("Working with subscription - '{}'", subscription);
      log.info("Amount of coubs in subscription object - '{}'", subscription.getCoubs().size());

      var lastPermalink = subscription.getLastPermalink();
      var coubs = subscription.getCoubs();

      coubs.sort((emp1, emp2) -> emp2.getPublishedAt().compareTo(emp1.getPublishedAt()));
      log.debug("Sorted coubs - {}", coubs);

      if (Objects.nonNull(lastPermalink)) {

        log.info("For subscription '{}-{}' lastPermalink is - '{}'. Trying to find unsent coubs",
            subscription.getCommunity().getName(), subscription.getSection().getName(),
            lastPermalink);

        var coubWithPermalink = coubs.stream()
            .filter(c -> c.getPermalink().equals(lastPermalink))
            .findFirst();

        log.info("Coub with lastPermalink - {}", coubWithPermalink);

        if (coubWithPermalink.isPresent()) {

          log.info("Found coub with lastPermalink in current list");
          log.info("Trying to sort and find unsent coubs");

          var lastCoub = coubWithPermalink.get();

          coubs.remove(lastCoub);

          coubs.removeIf(c -> c.getPublishedAt().isBefore(lastCoub.getPublishedAt()));

          log.debug("Coubs to send - {}", coubs);

        } else {

          log.info("Coub with lastPermalink not found. Trying to request more coubs.");
          // todo: если false то делаем доп запросы и мерджим списки
          // подумать как это реализовать

        }

      } else {

        log.info("No permalink found for subscription '{}-{}', take all found coubs",
            subscription.getCommunity().getName(),
            subscription.getSection().getName()
        );

        log.debug("Found last coub - {}", subscription.getCoubs());


      }

    }

    // remove subscriptions without coubs

    var toSend = subscriptions.stream()
        .filter(s -> !s.getCoubs().isEmpty())
        .toList();

    //send to message producer service

    log.info("Coubs to send for all subscriptions - {}", toSend);

    if (!toSend.isEmpty()) {
      messageProducerService.sendSubscriptionsData(toSend);
    }


  }


  private List<Coub> getCoubsForSubscription(String communityName, String section, int page,
      int perPage) {

    CoubWrapper coubs = coubService
        .getCoubsWrapperForCommunityAndSection(communityName, section,
            page,
            perPage);

    return coubs.getCoubs();

  }


}
