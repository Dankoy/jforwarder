package ru.dankoy.telegrambot.core.service.subscription;

import java.util.List;
import ru.dankoy.telegrambot.core.domain.subscription.community.CommunitySubscription;

public interface CommunitySubscriptionService {

  List<CommunitySubscription> getSubscriptionsByChatId(long chatId);

  CommunitySubscription subscribe(String communityName, String sectionName, long chatId);

  void unsubscribe(String communityName, String sectionName, long chatId);
}
