package ru.dankoy.telegrambot.core.service.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.telegrambot.core.domain.subscription.Chat;
import ru.dankoy.telegrambot.core.feign.subscriptionsholder.SubscriptionsHolderFeign;

@RequiredArgsConstructor
@Service
public class TelegramChatServiceImpl implements TelegramChatService {

  private final SubscriptionsHolderFeign subscriptionsHolderFeign;

  @Override
  public Chat getChatById(long chatId) {
    return subscriptionsHolderFeign.getChatById(chatId);
  }

  @Override
  public Chat createChat(Chat chat) {
    return subscriptionsHolderFeign.createChat(chat);
  }

  @Override
  public Chat update(Chat chat) {
    return subscriptionsHolderFeign.updateChat(chat);
  }
}
