package ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller.subscription.tag;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription.TagSubscriptionCreateDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription.TagSubscriptionDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.subscription.tag.TagSubService;

@RequiredArgsConstructor
@RestController
public class TagSubController {

  private final TagSubService tagSubService;

  @GetMapping(
      value = "/api/v1/tag_subscriptions",
      params = {"active"})
  public Page<TagSubscriptionDTO> getAllByActiveChat(
      @RequestParam("active") boolean active, Pageable pageable) {

    var subs = tagSubService.getAllByActiveTelegramChats(active, pageable);

    return subs.map(TagSubscriptionDTO::toDTO);
  }

  @GetMapping(
      value = "/api/v1/tag_subscriptions",
      params = {"telegramChatId"})
  public List<TagSubscriptionDTO> getAllByTelegramChat(
      @RequestParam("telegramChatId") long telegramChatId,
      @RequestParam(value = "messageThreadId", required = false) Integer messageThreadId) {

    var subs =
        tagSubService.getAllByTelegramChatIdAndMessageThreadId(telegramChatId, messageThreadId);

    return subs.stream().map(TagSubscriptionDTO::toDTO).toList();
  }

  @PostMapping(value = "/api/v1/tag_subscriptions")
  public TagSubscriptionDTO create(@Valid @RequestBody TagSubscriptionCreateDTO dto) {

    var ts = TagSubscriptionCreateDTO.fromDTO(dto);

    var sub = tagSubService.createSubscription(ts);

    return TagSubscriptionDTO.toDTO(sub);
  }

  @DeleteMapping(value = "/api/v1/tag_subscriptions")
  public void delete(@Valid @RequestBody TagSubscriptionCreateDTO dto) {

    var ts = TagSubscriptionCreateDTO.fromDTO(dto);

    tagSubService.deleteSubscription(ts);
  }

  @GetMapping(
      value = "/api/v1/tag_subscriptions",
      params = {"chatUuids"})
  public Page<TagSubscriptionDTO> getChats(
      @RequestParam(value = "chatUuids") List<UUID> chatUuids, Pageable pageable) {

    var page = tagSubService.findAllByChatsUUID(chatUuids, pageable);

    return page.map(TagSubscriptionDTO::toDTO);
  }
}
