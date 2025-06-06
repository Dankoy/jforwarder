package ru.dankoy.telegrambot.core.service.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.dankoy.telegrambot.core.gateway.BotMessageGateway;

@Slf4j
@RequiredArgsConstructor
public class TelegramBotIntegrationFlowImpl implements LongPollingSingleThreadUpdateConsumer {

  private final BotMessageGateway botMessageGateway;

  @Override
  public void consume(Update update) {

    if (update.hasMessage()) {

      Message message = update.getMessage();

      if (update.getMessage().hasText()) {

        log.info(
            "Received message from '{}' with text '{}'",
            message.getChat().getId(),
            message.getText());

        botMessageGateway.process(message);
      }
    }
  }
}
