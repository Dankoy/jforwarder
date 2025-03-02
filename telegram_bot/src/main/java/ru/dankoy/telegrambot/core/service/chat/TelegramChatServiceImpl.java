package ru.dankoy.telegrambot.core.service.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.telegrambot.core.domain.ChatWithUUID;
import ru.dankoy.telegrambot.core.feign.telegramchatservice.TelegramChatServiceFeign;

@RequiredArgsConstructor
@Service
public class TelegramChatServiceImpl implements TelegramChatService {

  private final TelegramChatServiceFeign telegramChatServiceFeign;

  @Override
  public ChatWithUUID getChatByIdAndMessageThreadId(long chatId, Integer messageThreadId) {
    return telegramChatServiceFeign.getChatByIdAndMessageThreadId(chatId, messageThreadId);
  }

  @Override
  public ChatWithUUID createChat(ChatWithUUID chat) {
    return telegramChatServiceFeign.createChat(chat);
  }

  @Override
  public ChatWithUUID update(ChatWithUUID chat) {
    return telegramChatServiceFeign.updateChat(chat.getId(), chat);
  }
}
