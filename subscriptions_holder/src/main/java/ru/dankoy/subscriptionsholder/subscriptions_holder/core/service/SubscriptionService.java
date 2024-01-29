package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.CommunitySubscription;

/**
 * @deprecated in favor of {@link CommunitySubService}
 */
@Deprecated(since = "2024-01-27")
public interface SubscriptionService {

  Page<CommunitySubscription> getAll(Pageable pageable);

  Page<CommunitySubscription> getAllWithActiveChats(boolean active, Pageable pageable);

  List<CommunitySubscription> getAllByCommunityName(String communityName);

  List<CommunitySubscription> getAllByChatId(long chatId);

  Optional<CommunitySubscription> getByCommunityNameSectionNameChatId(String communityName,
      String sectionName, long chatId);

  CommunitySubscription subscribeChatToCommunity(CommunitySubscription communitySubscription);

  void unsubscribeChatFromCommunity(CommunitySubscription communitySubscription);

  CommunitySubscription updateLastPermalink(CommunitySubscription communitySubscription);

}
