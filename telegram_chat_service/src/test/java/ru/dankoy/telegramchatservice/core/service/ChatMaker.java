package ru.dankoy.telegramchatservice.core.service;

import java.time.LocalDateTime;
import java.util.UUID;
import ru.dankoy.telegramchatservice.core.domain.Chat;
import ru.dankoy.telegramchatservice.core.domain.dto.ChatDTO;

public interface ChatMaker {

  default Chat makeChat(long chatId) {
    var date = LocalDateTime.now();

    return new Chat(
        0L, chatId, "type", "title", "firstName", "lastName", "username", true, 1, date, date);
  }

  default ChatDTO makeChat(UUID id, long chatId) {
    var date = LocalDateTime.now();

    return new ChatDTO(
        id, chatId, "type", "title", "firstName", "lastName", "username", true, 1, date, date);
  }
}
