package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.tag.TagSubscription;

public interface TagSubscriptionService {

  Page<TagSubscription> getAllByActiveTelegramChats(boolean active, Pageable pageable);

  List<TagSubscription> getAllByTelegramChatId(long telegramChatId);

  TagSubscription createSubscription(TagSubscription tagSubscription);

  void deleteSubscription(TagSubscription tagSubscription);

  TagSubscription updatePermalink(TagSubscription tagSubscription);

}
