package ru.dankoy.telegrambot.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.dankoy.telegrambot.core.service.bot.TelegramBot;
import ru.dankoy.telegrambot.core.service.bot.TelegramBotImpl;
import ru.dankoy.telegrambot.core.service.bot.commands.CommandsHolder;
import ru.dankoy.telegrambot.core.service.bot.commands.CommunitiesCommand;
import ru.dankoy.telegrambot.core.service.bot.commands.HelpCommand;
import ru.dankoy.telegrambot.core.service.bot.commands.MySubscriptionsCommand;
import ru.dankoy.telegrambot.core.service.bot.commands.StartCommand;
import ru.dankoy.telegrambot.core.service.bot.commands.SubscribeCommand;
import ru.dankoy.telegrambot.core.service.bot.commands.TagOrdersCommand;
import ru.dankoy.telegrambot.core.service.bot.commands.UnsubscribeCommand;
import ru.dankoy.telegrambot.core.service.bot.configuration.BotConfiguration;
import ru.dankoy.telegrambot.core.service.bot.configuration.BotConfigurationImpl;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatService;
import ru.dankoy.telegrambot.core.service.community.CommunityService;
import ru.dankoy.telegrambot.core.service.localization.LocalisationService;
import ru.dankoy.telegrambot.core.service.order.OrderService;
import ru.dankoy.telegrambot.core.service.subscription.CommunitySubscriptionService;
import ru.dankoy.telegrambot.core.service.subscription.TagSubscriptionService;
import ru.dankoy.telegrambot.core.service.template.TemplateBuilder;

@Configuration
@RequiredArgsConstructor
public class TelegramBotConfig {

  @Bean
  public TelegramBotsApi telegramBotsApi(TelegramBot telegramBot) throws TelegramApiException {

    var api = new TelegramBotsApi(DefaultBotSession.class);

    api.registerBot(telegramBot);

    return api;
  }

  @Bean
  public BotConfiguration botConfiguration(
      TelegramBotProperties properties,
      CommandsHolder commandsHolder,
      CommunitySubscriptionService communitySubscriptionService,
      TelegramChatService telegramChatService,
      TemplateBuilder templateBuilder,
      CommunityService communityService,
      TagSubscriptionService tagSubscriptionService,
      OrderService orderService,
      LocalisationService localisationService) {

    return BotConfigurationImpl.builder()
        .telegramBotProperties(properties)
        .commandsHolder(commandsHolder)
        .communitySubscriptionService(communitySubscriptionService)
        .telegramChatService(telegramChatService)
        .templateBuilder(templateBuilder)
        .communityService(communityService)
        .tagSubscriptionService(tagSubscriptionService)
        .orderService(orderService)
        .localisationService(localisationService)
        .build();
  }

  @Bean
  public TelegramBot telegramBot(BotConfiguration botConfiguration) {

    return new TelegramBotImpl(botConfiguration);
  }

  @Bean
  public CommandsHolder commandsHolder() {

    var commandsHolder = new CommandsHolder();
    commandsHolder.addCommand(new MySubscriptionsCommand());
    commandsHolder.addCommand(new StartCommand());
    commandsHolder.addCommand(new HelpCommand());
    commandsHolder.addCommand(new SubscribeCommand());
    commandsHolder.addCommand(new UnsubscribeCommand());
    commandsHolder.addCommand(new CommunitiesCommand());
    commandsHolder.addCommand(new TagOrdersCommand());

    return commandsHolder;
  }
}
