package ru.dankoy.telegrambot.config.bot.configuration.botflow;

import ru.dankoy.telegrambot.config.bot.properties.FullBotProperties;
import ru.dankoy.telegrambot.core.service.bot.commands.CommandsHolder;

public interface BotConfiguration {

  FullBotProperties fullBotProperties();

  CommandsHolder commandsHolder();
}
