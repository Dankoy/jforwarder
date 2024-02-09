package ru.dankoy.telegrambot.core.service.bot.configuration;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.dankoy.telegrambot.config.TelegramBotProperties;
import ru.dankoy.telegrambot.core.service.bot.commands.CommandsHolder;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatService;
import ru.dankoy.telegrambot.core.service.community.CommunityService;
import ru.dankoy.telegrambot.core.service.order.OrderService;
import ru.dankoy.telegrambot.core.service.subscription.CommunitySubscriptionService;
import ru.dankoy.telegrambot.core.service.subscription.TagSubscriptionService;
import ru.dankoy.telegrambot.core.service.template.TemplateBuilder;

@Builder
@Getter
@RequiredArgsConstructor
public record BotConfigurationImpl(
    TelegramBotProperties telegramBotProperties,
    CommandsHolder commandsHolder,
    CommunitySubscriptionService communitySubscriptionService,
    TelegramChatService telegramChatService,
    TemplateBuilder templateBuilder,
    CommunityService communityService,
    TagSubscriptionService tagSubscriptionService,
    OrderService orderService)
    implements BotConfiguration {}
