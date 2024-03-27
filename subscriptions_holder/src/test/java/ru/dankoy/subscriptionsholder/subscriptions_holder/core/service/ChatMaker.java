package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;

public interface ChatMaker {

  default Chat makeChat(long chatId) {

    return new Chat(0L, chatId, "type", "title", "firstName", "lastName", "username", true);
  }
}
