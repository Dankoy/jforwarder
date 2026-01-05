package ru.dankoy.tcoubsinitiator.core.httpservice.registry;

import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import ru.dankoy.tcoubsinitiator.core.domain.page.RestPageImpl;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.registry.SentCoubsRegistry;

@HttpExchange(url = "http://subscriptions-holder")
public interface SentCoubsRegistryHttpService {

  @GetExchange(url = "/api/v1/sent_coubs_registry")
  RestPageImpl<SentCoubsRegistry> getAll();

  @GetExchange(url = "/api/v1/sent_coubs_registry")
  RestPageImpl<SentCoubsRegistry> getAllBySubscriptionId(
      @RequestParam long subscriptionId, @RequestParam Pageable pageable);

  @GetExchange(url = "/api/v1/sent_coubs_registry")
  RestPageImpl<SentCoubsRegistry> getAllBySubscriptionIdAndDateAfter(
      @RequestParam long subscriptionId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
      @RequestParam Pageable pageable);

  @PostExchange(url = "/api/v1/sent_coubs_registry")
  SentCoubsRegistry create(@RequestBody SentCoubsRegistry sentCoubsRegistry);

  @DeleteExchange(url = "/api/v1/sent_coubs_registry/{id}")
  void delete(@PathVariable long id);
}
