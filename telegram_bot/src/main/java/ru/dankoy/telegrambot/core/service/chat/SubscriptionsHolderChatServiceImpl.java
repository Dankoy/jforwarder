package ru.dankoy.telegrambot.core.service.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.telegrambot.core.domain.Chat;
import ru.dankoy.telegrambot.core.feign.subscriptionsholder.SubscriptionsHolderFeign;

@Service
@RequiredArgsConstructor
public class SubscriptionsHolderChatServiceImpl implements SubscriptionsHolderChatService {

  private final SubscriptionsHolderFeign feign;

  @Override
  public Chat getChatByIdAndMessageThreadId(long chatId, Integer messageThreadId) {

    return feign.getChatByIdAndMessageThreadId(chatId, messageThreadId);
  }

  @Override
  public Chat createChat(Chat chat) {

    return feign.createChat(chat);
  }

  @Override
  public Chat update(Chat chat) {

    return feign.updateChat(chat.getId(), chat);
  }
}
