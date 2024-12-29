package ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.chat.ChatWithSubs;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.communitysub.ChatCreateDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.communitysub.ChatDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.communitysub.ChatUpdateDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.TelegramChatService;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.searchparser.SearchCriteriaParser;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.specifications.telegramchat.TelegramChatSearchCriteria;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.specifications.telegramchat.criteria.SearchCriteria;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.specifications.telegramchat.mapper.ChatSearchCriteriaToFilter;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

  private final TelegramChatService telegramChatService;
  private final ChatSearchCriteriaToFilter mapper;
  private final SearchCriteriaParser searchCriteriaParser;

  @GetMapping(value = "/api/v1/telegram_chat", params = { "with_subs", "search" })
  public PagedModel<ChatWithSubs> getAllChats(
      @RequestParam("with_subs") boolean withSubs, Pageable pageable,
      @RequestParam(value = "search", required = false) String search) {

    // implemented search using criteria builder

    var params = searchCriteriaParser.parse(search);

    log.info("{}", params);

    var page = telegramChatService.findAllChatsWithSubs(params, pageable);
    return new PagedModel<>(page);
  }

  @GetMapping(value = "/api/v1/telegram_chat")
  public PagedModel<ChatDTO> getChats(Pageable pageable, TelegramChatSearchCriteria searchCriteria) {

    // implemented filter by spring specifications

    var filter = mapper.toFilter(searchCriteria);

    var page = telegramChatService.findAll(filter, pageable);

    var pageWithDto = page.map(ChatDTO::toDTO);

    return new PagedModel<>(pageWithDto);
  }

  @GetMapping(value = "/api/v1/telegram_chat", params = { "chatId" })
  public ChatDTO getChatById(
      @RequestParam("chatId") long chatId,
      @RequestParam(value = "messageThreadId", required = false) Integer messageThreadId) {
    var chatOptional = telegramChatService.getByTelegramChatIdAndMessageThreadId(chatId, messageThreadId);

    var chat = chatOptional.orElseThrow(
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
