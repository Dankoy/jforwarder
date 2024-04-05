package ru.dankoy.telegrambot.core.service.subscription;

import ru.dankoy.telegrambot.core.domain.Chat;

public interface ChatMaker {

  default Chat makeChat(long chatId) {

    return new Chat(0L, chatId, "type", "title", "firstName", "lastName", "username", true, 1);
  }
}
