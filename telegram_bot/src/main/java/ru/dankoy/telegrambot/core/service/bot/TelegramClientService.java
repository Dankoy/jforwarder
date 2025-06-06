package ru.dankoy.telegrambot.core.service.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dankoy.telegrambot.config.bot.configuration.botflow.BotConfiguration;

public interface TelegramClientService {

  void sendMessage(SendMessage sendMessage);

  void registerCommands(BotConfiguration botConfiguration) throws TelegramApiException;

  void deregisterCommands(BotConfiguration botConfiguration) throws TelegramApiException;
}
