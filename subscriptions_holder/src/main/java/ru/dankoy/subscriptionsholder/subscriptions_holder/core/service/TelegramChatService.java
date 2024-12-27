package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.chat.ChatWithSubs;

public interface TelegramChatService {

  Page<ChatWithSubs> findAllChatsWithSubs(Pageable pageable);

  Page<Chat> findAll(Pageable pageable);

  List<Chat> saveAll(List<Chat> chats);

  Chat save(Chat chat);

  Chat update(Chat chat);

  void deleteChats(List<Chat> chats);

  Optional<Chat> getByTelegramChatId(long chatId);

  Optional<Chat> getByTelegramChatIdAndMessageThreadId(long chatId, Integer messageThreadId);
}
