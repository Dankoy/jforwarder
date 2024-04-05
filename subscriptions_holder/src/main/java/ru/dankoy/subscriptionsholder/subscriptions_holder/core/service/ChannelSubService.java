package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.ChannelSub;

public interface ChannelSubService {

  Page<ChannelSub> getAllByActiveTelegramChats(boolean active, Pageable pageable);

  List<ChannelSub> getAllByTelegramChatId(long telegramChatId);

  List<ChannelSub> getAllByTelegramChatIdAndMessageThreadId(
      long telegramChatId, Integer messageThreadId);

  ChannelSub createSubscription(ChannelSub channelSub);

  void deleteSubscription(ChannelSub channelSub);
}
