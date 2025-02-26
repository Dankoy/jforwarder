package ru.dankoy.telegramchatservice.core.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.telegramchatservice.core.domain.dto.ChatDTO;
import ru.dankoy.telegramchatservice.core.domain.dto.ChatWithSubs;
import ru.dankoy.telegramchatservice.core.service.specifications.telegramchat.criteria.SearchCriteria;
import ru.dankoy.telegramchatservice.core.service.specifications.telegramchat.filter.TelegramChatFilter;

public interface TelegramChatService {

  Page<ChatWithSubs> findAllChatsWithSubs(List<SearchCriteria> search, Pageable pageable);

  Page<ChatDTO> findAll(String search, Pageable pageable);

  Page<ChatDTO> findAll(TelegramChatFilter filter, Pageable pageable);

  List<ChatDTO> saveAll(List<ChatDTO> chats);

  ChatDTO save(ChatDTO chat);

  ChatDTO update(ChatDTO chat);

  void deleteChats(List<ChatDTO> chats);

  ChatDTO getByTelegramChatId(long chatId);

  ChatDTO getByTelegramChatIdAndMessageThreadId(long chatId, Integer messageThreadId);
}
