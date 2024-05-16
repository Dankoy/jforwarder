package ru.dankoy.telegrambot.core.service.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

public interface TelegramBot extends LongPollingBot {

  void sendMessage(SendMessage sendMessage);
}
