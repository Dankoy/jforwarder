package ru.dankoy.kafkamessageconsumer.core.service.consumer;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageconsumer.core.service.telegrambot.TelegramBotService;


@Slf4j
@RequiredArgsConstructor
public class SubscriptionMessageConsumerBotSender implements SubscriptionMessageConsumer {

  private final TelegramBotService telegramBotService;

  @Override
  public void accept(List<CommunitySubscriptionMessage> values) {
    for (var value : values) {
      log.info("Sending message: {}", value);
      telegramBotService.sendMessage(value);
      log.info("Message sent");
    }
  }
}
