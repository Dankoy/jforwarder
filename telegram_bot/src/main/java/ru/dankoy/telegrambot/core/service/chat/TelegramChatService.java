package ru.dankoy.telegrambot.core.service.chat;

import ru.dankoy.telegrambot.core.domain.ChatWithUUID;

public interface TelegramChatService {

  ChatWithUUID getChatByIdAndMessageThreadId(long chatId, Integer messageThreadId);

  ChatWithUUID createChat(ChatWithUUID chat);

  ChatWithUUID update(ChatWithUUID chat);
}
