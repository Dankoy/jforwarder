package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.CommunitySub;

public interface CommunitySubService {

  Page<CommunitySub> findAll(Pageable pageable);

  Page<CommunitySub> findAllByChatActive(boolean isActive, Pageable pageable);

  List<CommunitySub> getAllByChatChatId(long telegramChatId);

  Optional<CommunitySub> getByCommunityNameSectionNameChatId(String communityName,
      String sectionName, long chatId);

  @Transactional
  CommunitySub subscribeChatToCommunity(CommunitySub communitySubscription);

  @Transactional
  void unsubscribeChatFromCommunity(CommunitySub communitySubscription);

  @Transactional
  CommunitySub updateLastPermalink(CommunitySub communitySubscription);
}