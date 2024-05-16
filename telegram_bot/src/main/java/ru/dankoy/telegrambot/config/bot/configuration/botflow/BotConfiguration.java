package ru.dankoy.telegrambot.config.bot.configuration.botflow;

import ru.dankoy.telegrambot.config.bot.properties.FullBotProperties;
import ru.dankoy.telegrambot.core.gateway.MessageGateway;
import ru.dankoy.telegrambot.core.service.bot.commands.CommandsHolder;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatService;
import ru.dankoy.telegrambot.core.service.template.TemplateBuilder;

public interface BotConfiguration {

  FullBotProperties fullBotProperties();

  TelegramChatService telegramChatService();

  TemplateBuilder templateBuilder();

  CommandsHolder commandsHolder();

  MessageGateway messageGateway();
}
