package ru.dankoy.tcoubsinitiator.core.service.filter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.Coub;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.registry.SentCoubsRegistry;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.subscription.Subscription;
import ru.dankoy.tcoubsinitiator.core.service.registry.SentCoubsRegistryService;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilterByRegistryServiceImpl implements FilterByRegistryService {

  private final SentCoubsRegistryService sentCoubsRegistryService;

  @Override
  public void filterByRegistry(List<? extends Subscription> subscriptions) {

    for (var sub : subscriptions) {

      log.info("Filter by registry");
      // filter subscriptions by registry
      Set<SentCoubsRegistry> registry = sentCoubsRegistryService.getAllBySubscriptionIdAndDateTimeAfter(
          sub.getId(),
          LocalDateTime.now().minusMonths(1)
      );
      log.info("Found registry - {}", registry);

      Set<String> registryPermalinks = registry.stream()
          .map(SentCoubsRegistry::getCoubPermalink)
          .collect(Collectors.toSet());

      List<Coub> filtered = new ArrayList<>(
          sub.getCoubs().stream()
              .filter(c -> !registryPermalinks.contains(c.getPermalink()))
              .toList()
      );
      sub.setCoubs(filtered);

      log.info("Filtered done");

    }

  }
}
