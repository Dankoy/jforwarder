package ru.dankoy.coubconnector.coub_connector.core.service.sheduler;

// по заданному времени лезет в апи куба, проверяет свои подписки и создает сообщения


import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.dankoy.coubconnector.coub_connector.core.domain.coubcom.coub.Coub;
import ru.dankoy.coubconnector.coub_connector.core.domain.coubcom.coub.CoubWrapper;
import ru.dankoy.coubconnector.coub_connector.core.domain.subscribtionsholder.subscription.Subscription;
import ru.dankoy.coubconnector.coub_connector.core.service.coub.CoubService;
import ru.dankoy.coubconnector.coub_connector.core.service.messageproducerconnectorservice.MessageProducerService;
import ru.dankoy.coubconnector.coub_connector.core.service.subscription.SubscriptionService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerCoubService {

  private final SubscriptionService subscriptionService;
  private final CoubService coubService;
  private final MessageProducerService messageProducerService;

    @Scheduled(fixedDelay = 600000)
//  @Scheduled(fixedDelay = 10000)
  public void scheduledOperation() {

    List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
    log.info("Subscriptions - {}", subscriptions);

    int page = 1;
    int perPage = 25;
    for (Subscription subscription : subscriptions) {

      // this is cached, so we have to work with copy of this list
      List<Coub> coubs = getCoubsForSubscription(
          subscription.getCommunity().getName(),
          subscription.getSection().getName(),
          1,
          25);

      subscription.addCoubs(coubs);

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

        log.info("No permalink found for subscription '{}-{}', take last published coub",
            subscription.getCommunity().getName(),
            subscription.getSection().getName()
        );
        // фильтрует по времени и берет самый первый кубб

        List<Coub> newCoubs = Stream.of(coubs.get(0)).toList();
        subscription.setCoubs(newCoubs);

        log.debug("Found last coub - {}", newCoubs);

      }

    }

    //send to message producer service

    log.debug("Coubs to send for all subscriptions - {}", subscriptions);

    messageProducerService.sendSubscriptionsData(subscriptions);

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
