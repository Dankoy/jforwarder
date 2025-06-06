package ru.dankoy.telegrambot.config.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.dankoy.telegrambot.config.bot.properties.BotCredentialsConfig;
import ru.dankoy.telegrambot.core.service.bot.TelegramClientService;
import ru.dankoy.telegrambot.core.service.bot.TelegramClientServiceImpl;
import ru.dankoy.telegrambot.core.service.chat.SubscriptionsHolderChatService;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatService;

@RequiredArgsConstructor
@Configuration
public class TelegramClientConfig {

  private final BotCredentialsConfig botCredentialsConfig;

  @Bean
  public TelegramClient telegramClient() {
    var tokenString = botCredentialsConfig.getName() + ":" + botCredentialsConfig.getToken();
    return new OkHttpTelegramClient(tokenString);
  }

  @Bean
  public TelegramClientService telegramClientService(
      TelegramClient telegramClient,
      TelegramChatService telegramChatService,
      SubscriptionsHolderChatService subscriptionsHolderChatService) {
    return new TelegramClientServiceImpl(
        telegramClient, telegramChatService, subscriptionsHolderChatService);
  }
}
