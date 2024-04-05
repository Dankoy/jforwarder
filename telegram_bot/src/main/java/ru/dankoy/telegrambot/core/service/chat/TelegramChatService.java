package ru.dankoy.telegrambot.core.service.chat;

import ru.dankoy.telegrambot.core.domain.Chat;

public interface TelegramChatService {

  Chat getChatById(long chatId);

  Chat getChatByIdAndMessageThreadId(long chatId, Integer messageThreadId);

  Chat createChat(Chat chat);

  Chat update(Chat chat);
}
