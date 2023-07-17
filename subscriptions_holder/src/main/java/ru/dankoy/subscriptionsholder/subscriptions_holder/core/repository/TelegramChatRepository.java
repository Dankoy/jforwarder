package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.TelegramChat;

@RepositoryRestResource(path = "telegram-chats")
public interface TelegramChatRepository extends JpaRepository<TelegramChat, Long> {


  Optional<TelegramChat> findByChatId(long chatId);

}
