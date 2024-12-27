package ru.dankoy.subscriptions_scheduler.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.subscriptions_scheduler.core.dto.subscriptions.ChatDTO;
import ru.dankoy.subscriptions_scheduler.core.mapper.ChatMapper;
import ru.dankoy.subscriptions_scheduler.core.service.chat.ChatService;

@RestController
@RequiredArgsConstructor
public class ChatController {

  private final ChatService chatService;
  private final ChatMapper chatMapper;

  @GetMapping("/api/v1/telegram_chat")
  public PagedModel<ChatDTO> getAllChats(Pageable pageable) {

    var page = chatService.findAll(pageable);

    var dtoPage = page.map(chatMapper::tDto);

    return new PagedModel<>(dtoPage);
  }
}
