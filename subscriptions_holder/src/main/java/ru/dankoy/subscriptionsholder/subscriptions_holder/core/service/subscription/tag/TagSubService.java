package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.subscription.tag;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.TagSub;

public interface TagSubService {

  Page<TagSub> getAllByActiveTelegramChats(boolean active, Pageable pageable);

  List<TagSub> getAllByTelegramChatId(long telegramChatId);

  List<TagSub> getAllByTelegramChatIdAndMessageThreadId(
      long telegramChatId, Integer messageThreadId);

  TagSub createSubscription(TagSub tagSubscription);

  void deleteSubscription(TagSub tagSubscription);

  Page<TagSub> findAllByChatsUUID(List<UUID> chatUuids, Pageable pageable);
}
