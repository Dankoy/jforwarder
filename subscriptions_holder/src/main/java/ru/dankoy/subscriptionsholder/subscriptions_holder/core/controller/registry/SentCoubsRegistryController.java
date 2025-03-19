package ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller.registry;

import jakarta.validation.Valid;
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
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.sentcoubsregistry.SentCoubsRegistryCreateDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.sentcoubsregistry.SentCoubsRegistryDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.sentcoubsregistry.SentCoubsRegistryService;

@RestController
@RequiredArgsConstructor
public class SentCoubsRegistryController {

  private final SentCoubsRegistryService sentCoubsRegistryService;

  @GetMapping(value = "/api/v1/sent_coubs_registry")
  public Page<SentCoubsRegistryDTO> getAll(Pageable pageable) {
    var s = sentCoubsRegistryService.findAll(pageable);
    return s.map(SentCoubsRegistryDTO::toDTO);
  }

  @GetMapping(
      value = "/api/v1/sent_coubs_registry",
      params = {"subscriptionId"})
  public Page<SentCoubsRegistryDTO> getAllBySubscriptionId(
      @RequestParam(value = "subscriptionId", required = true) long subscriptionId,
      Pageable pageable) {
    var s = sentCoubsRegistryService.getAllBySubscriptionId(subscriptionId, pageable);
    return s.map(SentCoubsRegistryDTO::toDTO);
  }

  @GetMapping(
      value = "/api/v1/sent_coubs_registry",
      params = {"subscriptionId", "dateTime"})
  public Page<SentCoubsRegistryDTO> getAllBySubscriptionIdAndDateTimeAfter(
      @RequestParam(value = "subscriptionId", required = true) long subscriptionId,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
      Pageable pageable) {
    var s =
        sentCoubsRegistryService.getAllBySubscriptionIdAndDateTimeAfter(
            subscriptionId, dateTime, pageable);
    return s.map(SentCoubsRegistryDTO::toDTO);
  }

  @PostMapping(value = "/api/v1/sent_coubs_registry")
  @ResponseStatus(HttpStatus.CREATED)
  public SentCoubsRegistryCreateDTO create(@Valid @RequestBody SentCoubsRegistryCreateDTO dto) {
    var fromDto = SentCoubsRegistryCreateDTO.fromDTO(dto);
    var s = sentCoubsRegistryService.create(fromDto);
    return SentCoubsRegistryCreateDTO.toDTO(s);
  }

  @DeleteMapping(value = "/api/v1/sent_coubs_registry/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void delete(@PathVariable(name = "id") long id) {
    sentCoubsRegistryService.deleteById(id);
  }
}
