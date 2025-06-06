package ru.dankoy.telegrambot.config.bot.configuration.botflow;

import ru.dankoy.telegrambot.config.bot.properties.FullBotProperties;
import ru.dankoy.telegrambot.core.service.bot.commands.CommandsHolder;

public record BotConfigurationImpl(
    FullBotProperties fullBotProperties, CommandsHolder commandsHolder)
    implements BotConfiguration {}
