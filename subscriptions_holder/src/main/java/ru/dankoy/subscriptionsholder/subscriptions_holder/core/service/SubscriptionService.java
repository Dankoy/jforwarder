package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import java.util.Optional;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.CommunitySubscription;

public interface SubscriptionService {

  List<CommunitySubscription> getAll();

  List<CommunitySubscription> getAllWithActiveChats(boolean active);

  List<CommunitySubscription> getAllByCommunityName(String communityName);

  List<CommunitySubscription> getAllByChatId(long chatId);

  Optional<CommunitySubscription> getByCommunityNameSectionNameChatId(String communityName,
      String sectionName, long chatId);

  CommunitySubscription subscribeChatToCommunity(CommunitySubscription communitySubscription);

  void unsubscribeChatFromCommunity(CommunitySubscription communitySubscription);

  CommunitySubscription updateLastPermalink(CommunitySubscription communitySubscription);

}
