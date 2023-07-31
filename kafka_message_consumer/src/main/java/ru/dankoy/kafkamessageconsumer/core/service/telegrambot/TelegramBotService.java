package ru.dankoy.kafkamessageconsumer.core.service.telegrambot;

import ru.dankoy.kafkamessageconsumer.core.domain.message.CommunitySubscriptionMessage;

public interface TelegramBotService {

  void sendMessage(CommunitySubscriptionMessage communitySubscriptionMessage);

}

