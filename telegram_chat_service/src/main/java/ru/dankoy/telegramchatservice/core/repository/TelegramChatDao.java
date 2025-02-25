package ru.dankoy.telegramchatservice.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import ru.dankoy.telegramchatservice.core.domain.Chat;
import ru.dankoy.telegramchatservice.core.domain.dto.ChatDTO;
import ru.dankoy.telegramchatservice.core.domain.jooq.tables.records.ChatsRecord;
import ru.dankoy.telegramchatservice.core.specifications.telegramchat.criteria.SearchCriteria;
import ru.dankoy.telegramchatservice.core.specifications.telegramchat.filter.TelegramChatFilter;

public interface TelegramChatDao {

  Page<ChatDTO> findAll(List<SearchCriteria> searchParams, Pageable pageable);

  Optional<ChatDTO> findByChatId(long chatId);

  Optional<ChatDTO> findByChatIdAndMessageThreadId(long chatId, Integer threadId);

  Optional<ChatDTO> findForUpdateByChatIdAndMessageThreadId(long chatId, Integer threadId);

  Optional<ChatDTO> findForUpdateById(long id);

}
