package ru.dankoy.telegrambot.config.bot.configuration.deprecated;

import lombok.Builder;
import ru.dankoy.telegrambot.config.bot.properties.FullBotProperties;
import ru.dankoy.telegrambot.core.service.bot.commands.CommandsHolder;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatService;
import ru.dankoy.telegrambot.core.service.community.CommunityService;
import ru.dankoy.telegrambot.core.service.localeprovider.LocaleProvider;
import ru.dankoy.telegrambot.core.service.localization.LocalisationService;
import ru.dankoy.telegrambot.core.service.order.OrderService;
import ru.dankoy.telegrambot.core.service.subscription.ChannelSubscriptionService;
import ru.dankoy.telegrambot.core.service.subscription.CommunitySubscriptionService;
import ru.dankoy.telegrambot.core.service.subscription.TagSubscriptionService;
import ru.dankoy.telegrambot.core.service.template.TemplateBuilder;

@Builder
public record BotConfigurationImpl(
    FullBotProperties fullBotProperties,
    CommandsHolder commandsHolder,
    CommunitySubscriptionService communitySubscriptionService,
    TelegramChatService telegramChatService,
    TemplateBuilder templateBuilder,
    CommunityService communityService,
    TagSubscriptionService tagSubscriptionService,
    ChannelSubscriptionService channelSubscriptionService,
    OrderService orderService,
    LocalisationService localisationService,
    LocaleProvider localeProvider)
    implements BotConfiguration {}
