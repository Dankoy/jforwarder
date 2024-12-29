package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;

public interface TelegramChatRepository
    extends JpaRepository<Chat, Long>,
        TelegramChatRepositoryCustom,
        JpaSpecificationExecutor<Chat> {

  Page<Chat> findAll(Specification<Chat> spec, Pageable pageable);

  Optional<Chat> findByChatId(long chatId);

  Optional<Chat> findByChatIdAndMessageThreadId(long chatId, Integer threadId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
  Optional<Chat> findForUpdateByChatIdAndMessageThreadId(long chatId, Integer threadId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
  Optional<Chat> findForUpdateById(long id);
}
