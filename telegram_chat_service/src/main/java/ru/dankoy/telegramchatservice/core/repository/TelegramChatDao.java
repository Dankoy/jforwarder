package ru.dankoy.telegramchatservice.core.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.telegramchatservice.core.domain.dto.ChatDTO;
import ru.dankoy.telegramchatservice.core.service.specifications.telegramchat.criteria.SearchCriteria;

public interface TelegramChatDao {

  Page<ChatDTO> findAll(List<SearchCriteria> searchParams, Pageable pageable);

  Optional<ChatDTO> findByChatId(long chatId);

  Optional<ChatDTO> findByChatIdAndMessageThreadId(long chatId, Integer threadId);

  Optional<ChatDTO> findForUpdateByChatIdAndMessageThreadId(long chatId, Integer threadId);

  Optional<ChatDTO> findForUpdateById(long id);

  ChatDTO save(ChatDTO chat);

  ChatDTO update(ChatDTO chat);

  List<ChatDTO> saveAll(List<ChatDTO> chats);

  void deleteBatch(List<ChatDTO> chats);
}
