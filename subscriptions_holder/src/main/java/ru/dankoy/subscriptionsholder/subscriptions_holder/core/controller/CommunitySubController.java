package ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.communitysub.CommunitySubCreateDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.communitysub.CommunitySubDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.CommunitySubService;

@RequiredArgsConstructor
@RestController
public class CommunitySubController {

  private final CommunitySubService communitySubService;

  @GetMapping(value = "/api/v1/community_subscriptions")
  public Page<CommunitySubDTO> getAll(Pageable pageable) {
    var s = communitySubService.findAll(pageable);
    return s.map(CommunitySubDTO::toDTO);
  }

  @GetMapping(
      value = "/api/v1/community_subscriptions",
      params = {"active"})
  public Page<CommunitySubDTO> getAllWithActiveChats(
      @RequestParam(value = "active", defaultValue = "true") boolean active, Pageable pageable) {

    var s = communitySubService.findAllByChatActive(active, pageable);

    return s.map(CommunitySubDTO::toDTO);
  }

  @GetMapping(
      value = "/api/v1/community_subscriptions",
      params = {"telegramChatId"})
  public List<CommunitySubDTO> getAllByTelegramChatId(
      @RequestParam(value = "telegramChatId") long telegramChatId,
      @RequestParam(value = "messageThreadId", required = false) Integer messageThreadId) {

    var s =
        communitySubService.getAllByChatChatIdAndMessageThreadId(telegramChatId, messageThreadId);
    return s.stream().map(CommunitySubDTO::toDTO).toList();
  }

  @PostMapping(path = "/api/v1/community_subscriptions")
  public CommunitySubDTO subscribeChatToCommunity(@Valid @RequestBody CommunitySubCreateDTO dto) {

    var subscription = CommunitySubCreateDTO.fromDTO(dto);

    var s = communitySubService.subscribeChatToCommunity(subscription);

    return CommunitySubDTO.toDTO(s);
  }

  @DeleteMapping(path = "/api/v1/community_subscriptions")
  @ResponseStatus(code = HttpStatus.ACCEPTED)
  public void unsubscribeChat(@Valid @RequestBody CommunitySubCreateDTO dto) {

    var subscription = CommunitySubCreateDTO.fromDTO(dto);

    communitySubService.unsubscribeChatFromCommunity(subscription);
  }
}
