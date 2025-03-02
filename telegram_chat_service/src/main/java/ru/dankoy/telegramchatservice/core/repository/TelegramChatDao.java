package ru.dankoy.telegramchatservice.core.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.telegramchatservice.core.domain.dto.ChatDTO;
import ru.dankoy.telegramchatservice.core.domain.filter.TelegramChatFilter;
import ru.dankoy.telegramchatservice.core.domain.search.RegexSearchCriteria;

public interface TelegramChatDao {

  Page<ChatDTO> findAll(List<RegexSearchCriteria> searchParams, Pageable pageable);

  Page<ChatDTO> findAllFiltered(TelegramChatFilter filter, Pageable pageable);

  Optional<ChatDTO> findByChatId(long chatId);

  Optional<ChatDTO> findByChatIdAndMessageThreadId(long chatId, Integer threadId);

  Optional<ChatDTO> findForUpdateByChatIdAndMessageThreadId(long chatId, Integer threadId);

  Optional<ChatDTO> findForUpdateById(UUID id);

  ChatDTO save(ChatDTO chat);

  ChatDTO update(ChatDTO chat);

  List<ChatDTO> saveAll(List<ChatDTO> chats);

  void deleteBatch(List<ChatDTO> chats);
}
