package ru.dankoy.kafkamessageconsumer.core.service.telegrambot;

import ru.dankoy.kafkamessageconsumer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageconsumer.core.domain.message.TagSubscriptionMessage;

public interface TelegramBotService {

  void sendMessage(CommunitySubscriptionMessage communitySubscriptionMessage);

  void sendMessage(TagSubscriptionMessage tagSubscriptionMessage);

}

