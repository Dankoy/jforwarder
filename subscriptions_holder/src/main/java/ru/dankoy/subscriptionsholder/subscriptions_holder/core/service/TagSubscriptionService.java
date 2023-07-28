package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.tag.TagSubscription;

public interface TagSubscriptionService {

  List<TagSubscription> getAllByActiveTelegramChats(boolean active);

  List<TagSubscription> getAllByTelegramChatId(long telegramChatId);

  TagSubscription createSubscription(TagSubscription tagSubscription);

  void deleteSubscription(TagSubscription tagSubscription);

  TagSubscription updatePermalink(TagSubscription tagSubscription);

}
