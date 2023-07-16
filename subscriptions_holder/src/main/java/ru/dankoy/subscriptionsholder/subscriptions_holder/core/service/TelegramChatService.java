package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import java.util.Optional;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.TelegramChat;

public interface TelegramChatService {

  List<TelegramChat> saveAll(List<TelegramChat> chats);

  TelegramChat save(TelegramChat chat);

  void deleteChats(List<TelegramChat> chats);

  Optional<TelegramChat> getByTelegramChatId(String chatId);
}
