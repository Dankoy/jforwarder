package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import java.util.Optional;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;

public interface TelegramChatService {

  List<Chat> saveAll(List<Chat> chats);

  Chat save(Chat chat);

  void deleteChats(List<Chat> chats);

  Optional<Chat> getByTelegramChatId(long chatId);
}
