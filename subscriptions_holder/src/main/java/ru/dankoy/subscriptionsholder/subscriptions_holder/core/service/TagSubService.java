package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.TagSub;


public interface TagSubService {

  Page<TagSub> getAllByActiveTelegramChats(boolean active, Pageable pageable);

  List<TagSub> getAllByTelegramChatId(long telegramChatId);

  TagSub createSubscription(TagSub tagSubscription);

  void deleteSubscription(TagSub tagSubscription);

}
