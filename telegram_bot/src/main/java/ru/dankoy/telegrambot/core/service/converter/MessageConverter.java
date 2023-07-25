package ru.dankoy.telegrambot.core.service.converter;

import java.util.List;
import ru.dankoy.telegrambot.core.domain.message.SubscriptionMessage;
import ru.dankoy.telegrambot.core.domain.subscription.Subscription;

public interface MessageConverter {

  List<SubscriptionMessage> convert(Subscription subscription);

  Subscription convert(SubscriptionMessage subscriptionMessage);
}
