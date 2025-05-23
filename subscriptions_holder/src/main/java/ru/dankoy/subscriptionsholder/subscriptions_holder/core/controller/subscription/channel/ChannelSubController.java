package ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller.subscription.channel;

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
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.channelsub.ChannelSubscriptionCreateDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.channelsub.ChannelSubscriptionDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.subscription.channel.ChannelSubService;

@RequiredArgsConstructor
@RestController
public class ChannelSubController {

  private final ChannelSubService channelSubService;

  @GetMapping(
      value = "/api/v1/channel_subscriptions",
      params = {"active"})
  public Page<ChannelSubscriptionDTO> getAllByActiveChat(
      @RequestParam("active") boolean active, Pageable pageable) {

    var subs = channelSubService.getAllByActiveTelegramChats(active, pageable);

    return subs.map(ChannelSubscriptionDTO::toDTO);
  }

  @GetMapping(
      value = "/api/v1/channel_subscriptions",
      params = {"telegramChatId"})
  public List<ChannelSubscriptionDTO> getAllByTelegramChat(
      @RequestParam("telegramChatId") long telegramChatId,
      @RequestParam(value = "messageThreadId", required = false) Integer messageThreadId) {

    var subs =
        channelSubService.getAllByTelegramChatIdAndMessageThreadId(telegramChatId, messageThreadId);

    return subs.stream().map(ChannelSubscriptionDTO::toDTO).toList();
  }

  @PostMapping(value = "/api/v1/channel_subscriptions")
  public ChannelSubscriptionDTO create(@Valid @RequestBody ChannelSubscriptionCreateDTO dto) {

    var ts = ChannelSubscriptionCreateDTO.fromDTO(dto);

    var sub = channelSubService.createSubscription(ts);

    return ChannelSubscriptionDTO.toDTO(sub);
  }

  @DeleteMapping(value = "/api/v1/channel_subscriptions")
  public void delete(@Valid @RequestBody ChannelSubscriptionCreateDTO dto) {

    var ts = ChannelSubscriptionCreateDTO.fromDTO(dto);

    channelSubService.deleteSubscription(ts);
  }

  @GetMapping(
      value = "/api/v1/channel_subscriptions",
      params = {"chatUuids"})
  public Page<ChannelSubscriptionDTO> getChats(
      @RequestParam(value = "chatUuids") List<UUID> chatUuids, Pageable pageable) {

    var page = channelSubService.findAllByChatsUUID(chatUuids, pageable);

    return page.map(ChannelSubscriptionDTO::toDTO);
  }
}
