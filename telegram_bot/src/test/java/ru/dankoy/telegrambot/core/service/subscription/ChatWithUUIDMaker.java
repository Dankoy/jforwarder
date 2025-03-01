package ru.dankoy.telegrambot.core.service.subscription;

import java.util.UUID;
import ru.dankoy.telegrambot.core.domain.Chat;
import ru.dankoy.telegrambot.core.domain.ChatWithUUID;

public interface ChatWithUUIDMaker {

  default ChatWithUUID makeChat(UUID uuid, long chatId) {

    return new ChatWithUUID(
        uuid, chatId, "type", "title", "firstName", "lastName", "username", true, 1);
  }

  default ChatWithUUID makeChat(Chat chat) {

    return new ChatWithUUID(
        UUID.randomUUID(),
        chat.getChatId(),
        chat.getType(),
        chat.getTitle(),
        chat.getFirstName(),
        chat.getLastName(),
        chat.getUsername(),
        chat.isActive(),
        chat.getMessageThreadId());
  }
}
