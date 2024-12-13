package ru.dankoy.telegrambot.core.service.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.telegrambot.core.domain.Chat;
import ru.dankoy.telegrambot.core.feign.subscriptionsholder.SubscriptionsHolderFeign;

@RequiredArgsConstructor
@Service
public class TelegramChatServiceImpl implements TelegramChatService {

  private final SubscriptionsHolderFeign subscriptionsHolderFeign;

  /**
   * @deprecated for topics support via messageThreadId
   */
  @Deprecated(since = "2024-04-05", forRemoval = true)
  @Override
  public Chat getChatById(long chatId) {
    return subscriptionsHolderFeign.getChatById(chatId);
  }

  @Override
  public Chat getChatByIdAndMessageThreadId(long chatId, Integer messageThreadId) {
    return subscriptionsHolderFeign.getChatByIdAndMessageThreadId(chatId, messageThreadId);
  }

  @Override
  public Chat createChat(Chat chat) {
    return subscriptionsHolderFeign.createChat(chat);
  }

  @Override
  public Chat update(Chat chat) {
    return subscriptionsHolderFeign.updateChat(chat.getId(), chat);
  }
}
