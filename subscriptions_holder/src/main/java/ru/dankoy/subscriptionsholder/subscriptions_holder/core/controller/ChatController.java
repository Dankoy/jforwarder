package ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.communitysub.ChatCreateDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.communitysub.ChatDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.communitysub.ChatUpdateDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.TelegramChatService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

  private final TelegramChatService telegramChatService;

  @GetMapping(
      value = "/api/v1/telegram_chat",
      params = {"chatId"})
  public ChatDTO getChatById(
      @RequestParam("chatId") long chatId,
      @RequestParam(value = "messageThreadId", required = false) Integer messageThreadId) {
    var chatOptional =
        telegramChatService.getByTelegramChatIdAndMessageThreadId(chatId, messageThreadId);

    var chat =
        chatOptional.orElseThrow(
            () -> new ResourceNotFoundException(String.format("Chat not found - %d", chatId)));

    return ChatDTO.toDTO(chat);
  }

  @PostMapping("/api/v1/telegram_chat")
  public ChatDTO createChat(@RequestBody @Valid ChatCreateDTO chatCreateDTO) {

    var chat = ChatCreateDTO.fromDTO(chatCreateDTO);

    var saved = telegramChatService.save(chat);

    return ChatDTO.toDTO(saved);
  }

  @PutMapping("/api/v1/telegram_chat/{id}")
  public ChatDTO updateChat(
      @PathVariable("id") long id, @RequestBody @Valid ChatUpdateDTO chatDTO) {

    chatDTO.setId(id);

    var chat = ChatUpdateDTO.fromDTO(chatDTO);

    var saved = telegramChatService.update(chat);

    return ChatDTO.toDTO(saved);
  }
}
