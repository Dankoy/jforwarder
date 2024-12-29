package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.chat.ChatWithSubs;

public interface TelegramChatRepositoryCustom {

  Page<ChatWithSubs> findAllWithSubsBy(Pageable pageable);
}
