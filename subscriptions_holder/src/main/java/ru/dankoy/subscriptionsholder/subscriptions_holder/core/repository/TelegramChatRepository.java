package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;

public interface TelegramChatRepository extends JpaRepository<Chat, Long> {

  Optional<Chat> findByChatId(long chatId);

  Optional<Chat> findByChatIdAndMessageThreadId(long chatId, Integer threadId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
  Optional<Chat> findForUpdateByChatIdAndMessageThreadId(long chatId, Integer threadId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
  Optional<Chat> findForUpdateById(long id);
}
