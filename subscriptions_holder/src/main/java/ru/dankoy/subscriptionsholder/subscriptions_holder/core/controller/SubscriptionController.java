package ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller;


import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Community;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.CommunityCreateDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.CommunityDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.TelegramChatDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.CommunityService;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.TelegramChatService;

@RequiredArgsConstructor
@RestController
public class SubscriptionController {

  private final CommunityService communityService;
  private final TelegramChatService telegramChatService;


  @GetMapping(path = "/api/v1/subscriptions")
  public List<Community> getAllSubscriptions() {

    return communityService.getAll();

  }

  @GetMapping(path = "/api/v1/subscriptions/{name}")
  public List<Community> getSubscriptionByCommunityName(@PathVariable(name = "name") String name) {

    return communityService.getByName(name);

  }


  @PostMapping(path = "/api/v1/subscriptions")
  public CommunityDTO createSubscription(@Valid @RequestBody CommunityCreateDTO communityDTO) {

    // создает новый community
    // если community уже существует, то выбрасывается ошибка

    var community = CommunityCreateDTO.fromDTO(communityDTO);

    var created = communityService.create(community);

    return CommunityDTO.toDTO(created);

  }

  @PutMapping(path = "/api/v1/subscriptions/{communityName}")
  public CommunityDTO subscribeChatToCommunity(
      @PathVariable(name = "communityName") String communityName,
      @PathParam(value = "sectionName") String sectionName,
      @Valid @RequestBody TelegramChatDTO telegramChatDTO
  ) {

    // не может создать новый community
    // добавляет чат к существующему community и все
    // если чат существует в базе, то использует его
    // если чата нет в базе, то создает новую запись в таблице чатов

    var chat = TelegramChatDTO.fromDTO(telegramChatDTO);

    var updated = communityService.addChat(communityName, sectionName, chat);

    return CommunityDTO.toDTO(updated);

  }


  @DeleteMapping(path = "/api/v1/subscriptions/{communityName}/{sectionName}")
  @ResponseStatus(code = HttpStatus.ACCEPTED)
  public void deleteCommunity(
      @PathVariable(name = "communityName") String communityName,
      @PathVariable(name = "sectionName") String sectionName
  ) {

    communityService.delete(communityName, sectionName);

  }

  @PutMapping(path = "/api/v1/subscriptions/{communityName}/{externalChatId}")
  @ResponseStatus(code = HttpStatus.ACCEPTED)
  public void unsubscribeChat(
      @PathVariable(name = "communityName") String communityName,
      @PathParam(value = "sectionName") String sectionName,
      @PathVariable(name = "externalChatId") String externalChatId
  ) {

    var chat = telegramChatService.getByTelegramChatId(externalChatId);

    chat.ifPresent(c -> communityService.deleteChatFromCommunity(communityName, sectionName, c));

  }

}
