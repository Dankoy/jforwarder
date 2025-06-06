package ru.dankoy.telegrambot.config.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dankoy.telegrambot.config.bot.configuration.botflow.BotConfiguration;
import ru.dankoy.telegrambot.config.bot.configuration.botflow.BotConfigurationImpl;
import ru.dankoy.telegrambot.config.bot.properties.FullBotProperties;
import ru.dankoy.telegrambot.config.bot.properties.LocaleConfig;
import ru.dankoy.telegrambot.core.factory.commands.BotCommandsFactory;
import ru.dankoy.telegrambot.core.factory.commands.BotCommandsFactoryImpl;
import ru.dankoy.telegrambot.core.gateway.BotMessageGateway;
import ru.dankoy.telegrambot.core.service.bot.TelegramBotIntegrationFlowImpl;
import ru.dankoy.telegrambot.core.service.bot.TelegramClientService;
import ru.dankoy.telegrambot.core.service.bot.commands.CommandsHolder;
import ru.dankoy.telegrambot.core.service.chat.SubscriptionsHolderChatService;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatService;
import ru.dankoy.telegrambot.core.service.community.CommunityService;
import ru.dankoy.telegrambot.core.service.localization.LocalisationService;
import ru.dankoy.telegrambot.core.service.order.OrderService;
import ru.dankoy.telegrambot.core.service.subscription.ChannelSubscriptionService;
import ru.dankoy.telegrambot.core.service.subscription.CommunitySubscriptionService;
import ru.dankoy.telegrambot.core.service.subscription.TagSubscriptionService;

@Configuration
@RequiredArgsConstructor
public class TelegramBotFlowConfig {

  private final TelegramClientService telegramClientService;

  @Bean(initMethod = "registerBotCommands")
  public TelegramBotsLongPollingApplication telegramBotsApi(
      BotConfiguration botConfiguration, BotMessageGateway botMessageGateway)
      throws TelegramApiException {

    // Commands registration should be done after bot start

    // bot should be autoclosed by spring
    var botsApplication = new TelegramBotsLongPollingApplication();
    var bot = new TelegramBotIntegrationFlowImpl(botMessageGateway);

    botsApplication.registerBot(botConfiguration.fullBotProperties().getToken(), bot);

    return botsApplication;
  }

  public void registerBotCommands(BotConfiguration botConfiguration) throws TelegramApiException {

    telegramClientService.deregisterCommands(botConfiguration);
    telegramClientService.registerCommands(botConfiguration);
  }

  @Bean
  public BotConfiguration botConfiguration(
      FullBotProperties properties, CommandsHolder commandsHolder) {

    return new BotConfigurationImpl(properties, commandsHolder);
  }

  @Bean
  public List<BotCommandsFactory> botCommandsFactories(
      LocaleConfig localeConfig,
      LocalisationService localisationService,
      CommunitySubscriptionService communitySubscriptionService,
      TagSubscriptionService tagSubscriptionService,
      ChannelSubscriptionService channelSubscriptionService,
      TelegramChatService telegramChatService,
      CommunityService communityService,
      OrderService orderService,
      SubscriptionsHolderChatService subscriptionsHolderChatService) {

    List<BotCommandsFactory> factories = new ArrayList<>();

    for (Locale locale : localeConfig.getLocales()) {
      factories.add(
          new BotCommandsFactoryImpl(
              localisationService,
              locale,
              communitySubscriptionService,
              tagSubscriptionService,
              channelSubscriptionService,
              telegramChatService,
              communityService,
              orderService,
              subscriptionsHolderChatService));
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
