package ru.dankoy.telegrambot.core.service.subscription;

import java.util.List;
import ru.dankoy.telegrambot.core.domain.tagsubscription.TagSubscription;

public interface TagSubscriptionService {

  List<TagSubscription> getSubscriptionsByChatId(long chatId);

  TagSubscription subscribe(
      String tagName,
      String orderName,
      String scopeName,
      String typeName,
      long chatId);

  void unsubscribe(
      String tagName,
      String orderName,
      String scopeName,
      String typeName,
      long chatId
  );
}
