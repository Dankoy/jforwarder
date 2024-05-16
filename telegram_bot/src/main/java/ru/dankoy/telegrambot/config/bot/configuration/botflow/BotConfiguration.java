package ru.dankoy.telegrambot.config.bot.configuration.botflow;

import ru.dankoy.telegrambot.config.bot.properties.FullBotProperties;
import ru.dankoy.telegrambot.core.gateway.BotMessageGateway;
import ru.dankoy.telegrambot.core.service.bot.commands.CommandsHolder;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatService;

public interface BotConfiguration {

  FullBotProperties fullBotProperties();

  TelegramChatService telegramChatService();

  CommandsHolder commandsHolder();

  BotMessageGateway botMessageGateway();
}
