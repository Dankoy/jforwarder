package ru.dankoy.kafkamessageconsumer.core.service.consumer;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dankoy.kafkamessageconsumer.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.kafkamessageconsumer.core.service.telegrambot.TelegramBotService;

@Slf4j
@RequiredArgsConstructor
public class TagSubscriptionMessageConsumerBotSender implements TagSubscriptionMessageConsumer {

  private final TelegramBotService telegramBotService;

  @Override
  public void accept(List<TagSubscriptionMessage> values) {
    for (var value : values) {
      log.info("Sending message: {}", value);
      telegramBotService.sendTagMessage(value);
      log.info("Message sent");
    }
  }

  @Override
  public void accept(TagSubscriptionMessage value) {
    log.info("Sending message: {}", value);
    telegramBotService.sendTagMessage(value);
    log.info("Message sent");
  }
}
