package ru.dankoy.telegrambot.core.service.chat;

import ru.dankoy.telegrambot.core.domain.Chat;

public interface TelegramChatService {

    Chat getChatById(long chatId);

    Chat createChat(Chat chat);

    Chat update(Chat chat);
}
