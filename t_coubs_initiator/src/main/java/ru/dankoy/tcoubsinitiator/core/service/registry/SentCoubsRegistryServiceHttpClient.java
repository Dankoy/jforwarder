package ru.dankoy.tcoubsinitiator.core.service.registry;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.registry.SentCoubsRegistry;
import ru.dankoy.tcoubsinitiator.core.httpservice.registry.SentCoubsRegistryHttpService;

@Primary
@Service
@RequiredArgsConstructor
public class SentCoubsRegistryServiceHttpClient implements SentCoubsRegistryService {

  private static final int FIRST_PAGE = 0;
  private static final int PER_PAGE = 30;

  private final SentCoubsRegistryHttpService sentCoubsRegistryHttpService;

  @Override
  public Set<SentCoubsRegistry> getAllBySubscriptionIdAndDateTimeAfter(
      long subscriptionId, LocalDateTime dateTime) {

    Set<SentCoubsRegistry> sentCoubsRegistries = new HashSet<>();

    int currentPage = FIRST_PAGE;
    var totalPages = Integer.MAX_VALUE;

    var sort = Sort.by("id").ascending();

    while (currentPage <= totalPages) {

      var pageable = PageRequest.of(currentPage, PER_PAGE, sort);

      Page<SentCoubsRegistry> page =
          sentCoubsRegistryHttpService.getAllBySubscriptionIdAndDateAfter(
              subscriptionId, dateTime, pageable);

      totalPages = page.getTotalPages() - 1;

      sentCoubsRegistries.addAll(page.getContent());

      currentPage++;
    }

    return sentCoubsRegistries;
  }
}
