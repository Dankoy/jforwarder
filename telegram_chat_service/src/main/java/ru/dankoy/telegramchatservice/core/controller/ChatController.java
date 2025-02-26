package ru.dankoy.telegramchatservice.core.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.dankoy.telegramchatservice.core.domain.dto.ChatCreateDTO;
import ru.dankoy.telegramchatservice.core.domain.dto.ChatDTO;
import ru.dankoy.telegramchatservice.core.domain.dto.ChatUpdateDTO;
import ru.dankoy.telegramchatservice.core.domain.dto.ChatWithSubs;
import ru.dankoy.telegramchatservice.core.exceptions.ResourceNotFoundException;
import ru.dankoy.telegramchatservice.core.mapper.ChatMapper;
import ru.dankoy.telegramchatservice.core.service.TelegramChatService;
import ru.dankoy.telegramchatservice.core.service.searchparser.SearchCriteriaParser;
import ru.dankoy.telegramchatservice.core.service.specifications.telegramchat.TelegramChatSearchCriteria;
import ru.dankoy.telegramchatservice.core.service.specifications.telegramchat.mapper.ChatSearchCriteriaToFilter;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

  private final TelegramChatService telegramChatService;
  private final ChatSearchCriteriaToFilter mapper;
  private final SearchCriteriaParser searchCriteriaParser;
  private final ChatMapper chatMapper;

  @Deprecated(since = "2025-02-25")
  @GetMapping(value = "/api/v1/telegram_chat", params = { "with_subs", "search" })
  public Page<ChatWithSubs> getAllChats(
      @RequestParam("with_subs") boolean withSubs,
      Pageable pageable,
      @RequestParam(value = "search", required = false) String search) {

    // implemented search using criteria builder

    var params = searchCriteriaParser.parse(search);

    return telegramChatService.findAllChatsWithSubs(params, pageable);
  }

  @GetMapping(value = "/api/v1/telegram_chat", params = { "search" })
  public Page<ChatDTO> getChats(Pageable pageable, @RequestParam(value = "search", required = false) String search) {

    return telegramChatService.findAll(search, pageable);
  }

  @GetMapping(value = "/api/v1/telegram_chat")
  public Page<ChatDTO> getChats(Pageable pageable, TelegramChatSearchCriteria searchCriteria) {

    // implemented filter by spring specifications

    var filter = mapper.toFilter(searchCriteria);

    return telegramChatService.findAll(filter, pageable);
  }

  @GetMapping(value = "/api/v1/telegram_chat", params = { "chatId" })
  public ChatDTO getChatById(
      @RequestParam("chatId") long chatId,
      @RequestParam(value = "messageThreadId", required = false) Integer messageThreadId) {
    return telegramChatService.getByTelegramChatIdAndMessageThreadId(chatId, messageThreadId);
  }

  @PostMapping("/api/v1/telegram_chat")
  public ChatDTO createChat(@RequestBody @Valid ChatCreateDTO chatCreateDTO) {

    var dto = chatMapper.fromChatCreateDTO(chatCreateDTO);

    return telegramChatService.save(dto);
  }

  @PutMapping("/api/v1/telegram_chat/{id}")
  public ChatDTO updateChat(
      @PathVariable("id") long id, @RequestBody @Valid ChatUpdateDTO chatDTO) {

    chatDTO.setId(id);

    var dto = chatMapper.fromChatUpdateDTO(chatDTO);

    return telegramChatService.update(dto);

  }
}
