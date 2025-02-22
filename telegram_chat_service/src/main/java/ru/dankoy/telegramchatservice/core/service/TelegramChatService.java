package ru.dankoy.telegramchatservice.core.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.telegramchatservice.core.domain.Chat;
// import ru.dankoy.telegramchatservice.core.dto.chat.ChatWithSubs;
import ru.dankoy.telegramchatservice.core.specifications.telegramchat.criteria.SearchCriteria;
import ru.dankoy.telegramchatservice.core.specifications.telegramchat.filter.TelegramChatFilter;

public interface TelegramChatService {

  // Page<ChatWithSubs> findAllChatsWithSubs(List<SearchCriteria> search, Pageable pageable);

  Page<Chat> findAll(TelegramChatFilter filter, Pageable pageable);

  List<Chat> saveAll(List<Chat> chats);

  Chat save(Chat chat);

  Chat update(Chat chat);

  void deleteChats(List<Chat> chats);

  Optional<Chat> getByTelegramChatId(long chatId);

  Optional<Chat> getByTelegramChatIdAndMessageThreadId(long chatId, Integer messageThreadId);
}
