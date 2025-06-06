package ru.dankoy.telegrambot.config.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dankoy.telegrambot.config.bot.configuration.botflow.BotConfiguration;
import ru.dankoy.telegrambot.core.exceptions.BotException;
import ru.dankoy.telegrambot.core.service.bot.TelegramClientService;

@Slf4j
@RequiredArgsConstructor
@Component
public class TelegramBotLongPollingApplicationListener {

  private final TelegramClientService telegramClientService;
  private final BotConfiguration botConfiguration;

  @EventListener
  public void onApplicationEvent(ContextRefreshedEvent event) {

    try {

      telegramClientService.deregisterCommands(botConfiguration);
      telegramClientService.registerCommands(botConfiguration);
    } catch (TelegramApiException e) {
      var message = "Bot commands couldn't be registered";
      log.error(message, e);
      throw new BotException(message, e);
    }

    log.info("Bot commands registered");
  }
}
