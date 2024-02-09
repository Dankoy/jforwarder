package ru.dankoy.kafkamessageconsumer.core.service.consumer;

import java.util.List;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CoubMessage;

@Slf4j
@RequiredArgsConstructor
public class CoubMessageConsumerImpl implements CoubMessageConsumer {

  private final Consumer<CoubMessage> telegramBotServiceConsumer;

  @Override
  public void accept(List<? extends CoubMessage> value) {
    for (var v : value) {
      log.info("Sending message: {}", v);
      telegramBotServiceConsumer.accept(v);
      log.info("Message sent");
    }
  }

  @Override
  public void accept(CoubMessage value) {
    log.info("Sending message: {}", value);
    telegramBotServiceConsumer.accept(value);
    log.info("Message sent");
  }
}
