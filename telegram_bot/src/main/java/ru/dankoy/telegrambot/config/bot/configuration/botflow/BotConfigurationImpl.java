package ru.dankoy.telegrambot.config.bot.configuration.botflow;

import lombok.Builder;
import ru.dankoy.telegrambot.config.bot.properties.FullBotProperties;
import ru.dankoy.telegrambot.core.gateway.MessageGateway;
import ru.dankoy.telegrambot.core.service.bot.commands.CommandsHolder;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatService;
import ru.dankoy.telegrambot.core.service.template.TemplateBuilder;

@Builder
public record BotConfigurationImpl(
    FullBotProperties fullBotProperties,
    CommandsHolder commandsHolder,
    TelegramChatService telegramChatService,
    TemplateBuilder templateBuilder,
    MessageGateway messageGateway)
    implements BotConfiguration {}
