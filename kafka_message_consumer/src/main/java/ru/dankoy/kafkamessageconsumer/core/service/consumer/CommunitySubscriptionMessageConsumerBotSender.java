package ru.dankoy.kafkamessageconsumer.core.service.consumer;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageconsumer.core.service.telegrambot.TelegramBotService;

/**
 * @deprecated in favor {@link CoubMessageConsumer}
 */
@Deprecated(since = "2024-02-01")
@Slf4j
@RequiredArgsConstructor
public class CommunitySubscriptionMessageConsumerBotSender
    implements CommunitySubscriptionMessageConsumer {

  @Qualifier("telegramBotServiceHttpClient")
  private final TelegramBotService telegramBotService;

  @Override
  public void accept(List<CommunitySubscriptionMessage> values) {
    for (var value : values) {
      log.info("Sending message: {}", value);
      telegramBotService.sendCommunityMessage(value);
      log.info("Message sent");
    }
  }

  @Override
  public void accept(CommunitySubscriptionMessage value) {
    log.info("Sending message: {}", value);
    telegramBotService.sendCommunityMessage(value);
    log.info("Message sent");
  }
}
