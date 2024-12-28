package ru.dankoy.subscriptions_scheduler.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.subscriptions_scheduler.core.dto.subscriptions.ChatWithSubsDTO;
import ru.dankoy.subscriptions_scheduler.core.mapper.ChatWithSubsMapper;
import ru.dankoy.subscriptions_scheduler.core.service.chat.ChatService;

@RestController
@RequiredArgsConstructor
public class ChatController {

  private final ChatService chatService;
  private final ChatWithSubsMapper chatMapper;

  @GetMapping(
      value = "/api/v1/telegram_chat",
      params = {"with_subs"})
  public PagedModel<ChatWithSubsDTO> getAllChats(
      @RequestParam("with_subs") boolean withSubs, Pageable pageable) {

    var page = chatService.findAll(withSubs, pageable);

    var dtoPage = page.map(chatMapper::toDto);

    return new PagedModel<>(dtoPage);
  }
}
