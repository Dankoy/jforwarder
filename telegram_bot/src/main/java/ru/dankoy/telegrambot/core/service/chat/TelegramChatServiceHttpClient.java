package ru.dankoy.telegrambot.core.service.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.telegrambot.core.domain.ChatWithUUID;
import ru.dankoy.telegrambot.core.httpservice.telegramchatservice.TelegramChatHttpService;

@RequiredArgsConstructor
@Service("telegramChatServiceHttpClient")
public class TelegramChatServiceHttpClient implements TelegramChatService {

  private final TelegramChatHttpService chatService;

  @Override
  public ChatWithUUID getChatByIdAndMessageThreadId(long chatId, Integer messageThreadId) {
    return chatService.getChatByIdAndMessageThreadId(chatId, messageThreadId);
  }

  @Override
  public ChatWithUUID createChat(ChatWithUUID chat) {
    return chatService.createChat(chat);
  }

  @Override
  public ChatWithUUID update(ChatWithUUID chat) {
    return chatService.updateChat(chat.getId(), chat);
  }
}
