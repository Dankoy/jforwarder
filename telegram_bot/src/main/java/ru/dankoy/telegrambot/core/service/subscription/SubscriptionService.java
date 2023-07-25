package ru.dankoy.telegrambot.core.service.subscription;

import java.util.List;
import ru.dankoy.telegrambot.core.domain.subscription.Subscription;

public interface SubscriptionService {

  List<Subscription> getSubscriptionsByChatId(long chatId);

  Subscription subscribe(String communityName, String sectionName, long chatId);

  void unsubscribe(String communityName, String sectionName, long chatId);

}
