package ru.dankoy.telegrambot.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.dankoy.telegrambot.core.service.bot.TelegramBotImpl;
import ru.dankoy.telegrambot.core.service.bot.commands.CommandsHolder;
import ru.dankoy.telegrambot.core.service.bot.commands.CommunitiesCommand;
import ru.dankoy.telegrambot.core.service.bot.commands.HelpCommand;
import ru.dankoy.telegrambot.core.service.bot.commands.MySubscriptionsCommand;
import ru.dankoy.telegrambot.core.service.bot.commands.StartCommand;
import ru.dankoy.telegrambot.core.service.bot.commands.SubscribeCommand;
import ru.dankoy.telegrambot.core.service.bot.commands.UnsubscribeCommand;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatService;
import ru.dankoy.telegrambot.core.service.community.CommunityService;
import ru.dankoy.telegrambot.core.service.subscription.SubscriptionService;
import ru.dankoy.telegrambot.core.service.template.TemplateBuilder;

@Configuration
@RequiredArgsConstructor
public class TelegramBotConfig {


  @Bean
  TelegramBotsApi telegramBotsApi(TelegramBotImpl telegramBotImpl) throws TelegramApiException {

    var api = new TelegramBotsApi(DefaultBotSession.class);

    api.registerBot(telegramBotImpl);

    return api;
  }

  @Bean
  TelegramBotImpl telegramBot(
      TelegramBotProperties properties,
      CommandsHolder commandsHolder,
      SubscriptionService subscriptionService,
      TelegramChatService telegramChatService,
      TemplateBuilder templateBuilder,
      CommunityService communityService
  ) {

    return new TelegramBotImpl(
        properties.getName(),
        properties.getToken(),
        commandsHolder.getCommands(),
        subscriptionService,
        telegramChatService,
        templateBuilder,
        communityService
    );
  }


  @Bean
  CommandsHolder commandsHolder() {

    var commandsHolder = new CommandsHolder();
    commandsHolder.addCommand(new MySubscriptionsCommand());
    commandsHolder.addCommand(new StartCommand());
    commandsHolder.addCommand(new HelpCommand());
    commandsHolder.addCommand(new SubscribeCommand());
    commandsHolder.addCommand(new UnsubscribeCommand());
    commandsHolder.addCommand(new CommunitiesCommand());

    return commandsHolder;
  }


}
