package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.time.LocalDateTime;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;

public interface ChatMaker {

  default Chat makeChat(long chatId) {
    var date = LocalDateTime.now();

    return new Chat(
        0L, chatId, "type", "title", "firstName", "lastName", "username", true, 1, date, date);
  }
}
