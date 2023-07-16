package ru.dankoy.coubconnector.coub_connector.core.service.sheduler;

// по заданному времени лезет в апи куба, проверяет свои подписки и создает сообщения


import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.dankoy.coubconnector.coub_connector.core.domain.coubcom.coub.Coub;
import ru.dankoy.coubconnector.coub_connector.core.domain.coubcom.coub.CoubWrapper;
import ru.dankoy.coubconnector.coub_connector.core.domain.subscriberholder.subscription.Subscription;
import ru.dankoy.coubconnector.coub_connector.core.feign.coub.CoubFeign;
import ru.dankoy.coubconnector.coub_connector.core.feign.subscription.SubscriptionFeign;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerCoubProducer {


  private final SubscriptionFeign subscriptionFeign;
  private final CoubFeign coubFeign;


  @Scheduled(fixedDelay = 600000)
  public void scheduledOperation() {

    List<Subscription> subscriptions = getAllActiveSubscriptions();
    log.info("Subscriptions - {}", subscriptions);

    HashMap<Subscription, List<Coub>> coubsToSend = new HashMap<>();

    int page = 1;
    for (Subscription subscription : subscriptions) {

      var section = subscription.getSection();
      List<Coub> coubs = getCoubsForSubscription(subscription.getName(), section.getName(), 1);

      coubsToSend.put(subscription, coubs);

    }

    log.info("Coubs for subscriptions - {}", coubsToSend);

    // поиск кубов из апи с last_permalink

    for (Entry<Subscription, List<Coub>> entry : coubsToSend.entrySet()) {

      var subscription = entry.getKey();
      var lastPermalink = subscription.getLastPermalink();

      var coubs = entry.getValue();

      coubs.sort((emp1, emp2) -> emp2.getPublishedAt().compareTo(emp1.getPublishedAt()));
      log.info("Sorted coubs - {}", coubs);

      if (Objects.nonNull(lastPermalink)) {

        log.info("For subscription '{}-{}' lastPermalink is - '{}'. Trying to find unsent coubs",
            subscription.getName(), subscription.getSection().getName(), lastPermalink);

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

          log.info("Coubs to send - {}", coubs);

        } else {

          log.info("Coub with lastPermalink not found. Trying to request more coubs.");
          // todo: если false то делаем доп запросы и мерджим списки
          // подумать как это реализовать

        }

      } else {

        log.info("No permalink found for subscription '{}-{}', take published coub",
            subscription.getName(),
            subscription.getSection().getName()
        );
        // фильтрует по времени и берет самый первый кубб

        List<Coub> newCoubs = List.of(coubs.get(0));
        entry.setValue(newCoubs);

        log.info("Found last coub - {}", newCoubs);

      }


    }


  }


  private List<Coub> getCoubsForSubscription(String communityName, String section, int page) {

    CoubWrapper coubs = coubFeign.getCoubsForCommunityWrapperPageable(communityName, section, page);
    return coubs.getCoubs();

  }


  private List<Subscription> getAllActiveSubscriptions() {

    return subscriptionFeign.getAllSubscriptions();

  }


}
