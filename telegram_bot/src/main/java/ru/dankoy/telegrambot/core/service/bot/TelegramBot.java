package ru.dankoy.telegrambot.core.service.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import ru.dankoy.telegrambot.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.telegrambot.core.domain.message.TagSubscriptionMessage;

public interface TelegramBot extends LongPollingBot {

  void sendMessage(CommunitySubscriptionMessage message);

  void sendMessage(TagSubscriptionMessage message);

  void sendMessage(SendMessage sendMessage);
}
