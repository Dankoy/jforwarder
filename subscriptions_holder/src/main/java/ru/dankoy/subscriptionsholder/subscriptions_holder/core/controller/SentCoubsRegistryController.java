package ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller;


import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.registry.SentCoubsRegistry;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.SentCoubsRegistryService;


@RestController
@RequiredArgsConstructor
public class SentCoubsRegistryController {

  private final SentCoubsRegistryService sentCoubsRegistryService;


  @GetMapping(value = "/api/v1/sent_coubs_registry")
  public Page<SentCoubsRegistry> getAll(Pageable pageable) {
    return sentCoubsRegistryService.findAll(pageable);
  }


  @GetMapping(value = "/api/v1/sent_coubs_registry", params = {"subscriptionId"})
  public Page<SentCoubsRegistry> getAllBySubscriptionId(
      @RequestParam(value = "subscriptionId", required = true) long subscriptionId,
      Pageable pageable) {
    return sentCoubsRegistryService.getAllBySubscriptionId(subscriptionId, pageable);
  }

  @GetMapping(value = "/api/v1/sent_coubs_registry", params = {"subscriptionId", "dateTime"})
  public Page<SentCoubsRegistry> getAllBySubscriptionIdAndDateTimeAfter(
      @RequestParam(value = "subscriptionId", required = true) long subscriptionId,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
      Pageable pageable) {
    return sentCoubsRegistryService.getAllBySubscriptionIdAndDateTimeAfter(subscriptionId, dateTime,
        pageable);
  }

  @PostMapping(value = "/api/v1/sent_coubs_registry")
  public SentCoubsRegistry create(@RequestBody SentCoubsRegistry sentCoubsRegistry) {
    return sentCoubsRegistryService.create(sentCoubsRegistry);
  }

  @DeleteMapping(value = "/api/v1/sent_coubs_registry/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void delete(@PathVariable(name = "id") long id) {
    sentCoubsRegistryService.deleteById(id);
  }


}
