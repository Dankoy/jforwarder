package ru.dankoy.subscriptions_scheduler.core.service.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.subscriptions_scheduler.core.domain.subscribtionsholder.Chat;

public interface ChatService {

  Page<Chat> findAll(boolean withSubs, Pageable pageable);
}
