package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import java.util.Optional;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.TagSubscription;

public interface TagSubscriptionService {

  Optional<TagSubscription> getByTagTitleAndTelegramChatId(String title, long telegramChatId);

  List<TagSubscription> getAllByActiveTelegramChat(boolean active);

  TagSubscription createSubscription(TagSubscription tagSubscription);

  void deleteSubscription(TagSubscription tagSubscription);

  TagSubscription updatePermalink(TagSubscription tagSubscription);

}
