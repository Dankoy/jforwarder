package ru.dankoy.telegrambot.core.service.bot;

import ru.dankoy.telegrambot.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.telegrambot.core.domain.message.TagSubscriptionMessage;

public interface TelegramBot {

  void sendMessage(CommunitySubscriptionMessage message);

  void sendMessage(TagSubscriptionMessage message);

}
