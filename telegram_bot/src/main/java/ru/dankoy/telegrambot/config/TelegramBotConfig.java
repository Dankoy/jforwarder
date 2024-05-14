package ru.dankoy.telegrambot.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.dankoy.telegrambot.config.configuration.BotConfiguration;
import ru.dankoy.telegrambot.config.configuration.BotConfigurationImpl;
import ru.dankoy.telegrambot.core.factory.commands.BotCommandsFactory;
import ru.dankoy.telegrambot.core.factory.commands.BotCommandsFactoryImpl;
import ru.dankoy.telegrambot.core.gateway.MessageGateway;
import ru.dankoy.telegrambot.core.service.bot.TelegramBot;
import ru.dankoy.telegrambot.core.service.bot.TelegramBotIntegrationFlowImpl;
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
      FullBotProperties properties,
      CommandsHolder commandsHolder,
      CommunitySubscriptionService communitySubscriptionService,
      TelegramChatService telegramChatService,
      TemplateBuilder templateBuilder,
      CommunityService communityService,
      TagSubscriptionService tagSubscriptionService,
      OrderService orderService,
      LocalisationService localisationService,
      LocaleProvider localeProvider,
      ChannelSubscriptionService channelSubscriptionService,
      MessageGateway messageGateway) {

    return BotConfigurationImpl.builder()
        .fullBotProperties(properties)
        .commandsHolder(commandsHolder)
        .communitySubscriptionService(communitySubscriptionService)
        .telegramChatService(telegramChatService)
        .templateBuilder(templateBuilder)
        .communityService(communityService)
        .tagSubscriptionService(tagSubscriptionService)
        .orderService(orderService)
        .localisationService(localisationService)
        .localeProvider(localeProvider)
        .channelSubscriptionService(channelSubscriptionService)
        .messageGateway(messageGateway)
        .build();
  }

  @Bean
  public TelegramBot telegramBot(BotConfiguration botConfiguration) {

    return new TelegramBotIntegrationFlowImpl(botConfiguration);
  }

  @Bean
  public List<BotCommandsFactory> botCommandsFactories(
      LocaleConfig localeConfig,
      LocalisationService localisationService,
      CommunitySubscriptionService communitySubscriptionService,
      TagSubscriptionService tagSubscriptionService,
      ChannelSubscriptionService channelSubscriptionService,
      TelegramChatService telegramChatService) {

    List<BotCommandsFactory> factories = new ArrayList<>();

    for (Locale locale : localeConfig.getLocales()) {
      factories.add(
          new BotCommandsFactoryImpl(
              localisationService,
              locale,
              communitySubscriptionService,
              tagSubscriptionService,
              channelSubscriptionService,
              telegramChatService));
    }

    return factories;
  }

  @Bean
  public CommandsHolder commandsHolder(List<BotCommandsFactory> botCommandsFactories) {

    var commandsHolder = new CommandsHolder();

    for (BotCommandsFactory factory : botCommandsFactories) {

      var commands = factory.allKnownCommands();

      commandsHolder.addCommands(factory.locale(), commands);
    }

    return commandsHolder;
  }
}
