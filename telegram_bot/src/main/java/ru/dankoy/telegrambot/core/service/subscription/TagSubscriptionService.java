package ru.dankoy.telegrambot.core.service.subscription;

import java.util.List;
import ru.dankoy.telegrambot.core.domain.subscription.tag.TagSubscription;

public interface TagSubscriptionService {

  List<TagSubscription> getSubscriptionsByChatId(long chatId);

  List<TagSubscription> getSubscriptionsByChatIdAndMessageThreadId(
      long chatId, Integer messageThreadId);

  List<TagSubscription> getSubsByChatIdAndMessageThreadId(long chatId, Integer messageThreadId);

  TagSubscription subscribe(
      String tagName,
      String orderValue,
      String scopeName,
      String typeName,
      long chatId,
      Integer messageThreadId);

  void unsubscribe(
      String tagName,
      String orderValue,
      String scopeName,
      String typeName,
      long chatId,
      Integer messageThreadId);
}
