package ru.dankoy.telegrambot.core.service.bot;

import ru.dankoy.telegrambot.core.domain.message.SubscriptionMessage;

public interface TelegramBot {

  void sendMessage(SubscriptionMessage message);

}
