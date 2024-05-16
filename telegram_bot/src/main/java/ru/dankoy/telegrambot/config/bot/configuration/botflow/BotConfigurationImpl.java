package ru.dankoy.telegrambot.config.bot.configuration.botflow;

import lombok.Builder;
import ru.dankoy.telegrambot.config.bot.properties.FullBotProperties;
import ru.dankoy.telegrambot.core.gateway.BotMessageGateway;
import ru.dankoy.telegrambot.core.service.bot.commands.CommandsHolder;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatService;

@Builder
public record BotConfigurationImpl(
    FullBotProperties fullBotProperties,
    CommandsHolder commandsHolder,
    TelegramChatService telegramChatService,
    BotMessageGateway botMessageGateway)
    implements BotConfiguration {}
