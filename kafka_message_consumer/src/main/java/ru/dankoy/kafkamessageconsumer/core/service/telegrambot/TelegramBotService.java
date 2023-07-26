package ru.dankoy.kafkamessageconsumer.core.service.telegrambot;

import ru.dankoy.kafkamessageconsumer.core.domain.message.SubscriptionMessage;

public interface TelegramBotService {

  void sendMessage(SubscriptionMessage subscriptionMessage);

}

