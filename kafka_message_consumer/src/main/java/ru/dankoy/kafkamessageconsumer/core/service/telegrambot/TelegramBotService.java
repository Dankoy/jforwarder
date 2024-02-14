package ru.dankoy.kafkamessageconsumer.core.service.telegrambot;

import ru.dankoy.kafkamessageconsumer.core.domain.message.CoubMessage;

public interface TelegramBotService {

  void sendCommunityMessage(CoubMessage communitySubscriptionMessage);

  void sendTagMessage(CoubMessage tagSubscriptionMessage);
  void sendChannelMessage(CoubMessage channelSubscriptionMessage);
}
