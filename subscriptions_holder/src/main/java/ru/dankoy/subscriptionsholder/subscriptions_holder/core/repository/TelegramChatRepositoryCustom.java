package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.chat.ChatWithSubs;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.specifications.telegramchat.criteria.SearchCriteria;

public interface TelegramChatRepositoryCustom {

  Page<ChatWithSubs> findAllWithSubsBy(Pageable pageable);

  Page<ChatWithSubs> findAllWithSubsByCriteria(List<SearchCriteria> search, Pageable pageable);
}
