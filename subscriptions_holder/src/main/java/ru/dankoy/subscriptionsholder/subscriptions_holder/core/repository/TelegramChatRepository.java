package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;

public interface TelegramChatRepository extends JpaRepository<Chat, Long> {

  Optional<Chat> findByChatId(long chatId);

}
