package ru.dankoy.telegrambot.core.service.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.telegrambot.core.domain.Chat;
import ru.dankoy.telegrambot.core.httpservice.subscriptionsholder.SubscriptionsHolderChatHttpService;

@Service("subscriptionsHolderChatServiceHttpClient")
@RequiredArgsConstructor
public class SubscriptionsHolderChatServiceHttpClient implements SubscriptionsHolderChatService {

  private final SubscriptionsHolderChatHttpService chatService;

  @Override
  public Chat getChatByIdAndMessageThreadId(long chatId, Integer messageThreadId) {

    return chatService.getChatByIdAndMessageThreadId(chatId, messageThreadId);
  }

  @Override
  public Chat createChat(Chat chat) {

    return chatService.createChat(chat);
  }

  @Override
  public Chat update(Chat chat) {

    return chatService.updateChat(chat.getId(), chat);
  }
}
