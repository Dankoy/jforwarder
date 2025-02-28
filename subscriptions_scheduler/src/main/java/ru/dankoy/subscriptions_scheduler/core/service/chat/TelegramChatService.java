package ru.dankoy.subscriptions_scheduler.core.service.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.subscriptions_scheduler.core.domain.telegramchatservice.ChatWithUUID;

public interface TelegramChatService {

  Page<ChatWithUUID> findAll(Pageable pageable);

  void update(ChatWithUUID chat);
}
