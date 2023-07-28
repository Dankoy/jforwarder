package ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller;


import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription.TagSubscriptionCreateDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription.TagSubscriptionDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.TagSubscriptionService;


@RequiredArgsConstructor
@RestController
public class TagSubscriptionController {

  private final TagSubscriptionService tagSubscriptionService;


  @GetMapping(value = "/api/v1/tag_subscriptions", params = {"active"})
  public List<TagSubscriptionDTO> getAllByActiveChat(@RequestParam("active") boolean active) {

    var subs = tagSubscriptionService.getAllByActiveTelegramChats(active);

    return subs.stream()
        .map(TagSubscriptionDTO::toDTO)
        .toList();

  }

  @GetMapping(value = "/api/v1/tag_subscriptions", params = {"telegramChatId"})
  public List<TagSubscriptionDTO> getAllByTelegramChat(
      @RequestParam("telegramChatId") long telegramChatId) {

    var subs = tagSubscriptionService.getAllByTelegramChatId(telegramChatId);

    return subs.stream()
        .map(TagSubscriptionDTO::toDTO)
        .toList();
  }


  @PostMapping(value = "/api/v1/tag_subscriptions")
  public TagSubscriptionDTO create(@Valid @RequestBody TagSubscriptionCreateDTO dto) {

    var ts = TagSubscriptionCreateDTO.fromDTO(dto);

    var sub = tagSubscriptionService.createSubscription(ts);

    return TagSubscriptionDTO.toDTO(sub);

  }

  @DeleteMapping(value = "/api/v1/tag_subscriptions")
  public void delete(@Valid @RequestBody TagSubscriptionCreateDTO dto) {

    var ts = TagSubscriptionCreateDTO.fromDTO(dto);

    tagSubscriptionService.deleteSubscription(ts);

  }


  @PutMapping("/api/v1/tag_subscriptions")
  public TagSubscriptionDTO updatePermalink(@Valid @RequestBody TagSubscriptionCreateDTO dto) {

    var ts = TagSubscriptionCreateDTO.fromDTO(dto);

    var sub = tagSubscriptionService.updatePermalink(ts);

    return TagSubscriptionDTO.toDTO(sub);

  }

}
