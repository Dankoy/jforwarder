package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import java.util.Optional;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Subscription;

public interface SubscriptionService {

  List<Subscription> getAll();

  List<Subscription> getAllByCommunityName(String communityName);

  List<Subscription> getAllByChatId(long chatId);

  Optional<Subscription> getByCommunityNameSectionNameChatId(String communityName,
      String sectionName, long chatId);

  Subscription subscribeChatToCommunity(Subscription subscription);

  void unsubscribeChatFromCommunity(Subscription subscription);

  Subscription updateLastPermalink(Subscription subscription);

}
