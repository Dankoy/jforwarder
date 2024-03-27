package ru.dankoy.kafkamessageconsumer.core.service.consumer;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dankoy.kafkamessageconsumer.core.service.registry.SentCoubsRegistryService;
import ru.dankoy.kafkamessageconsumer.core.service.subscription.SubscriptionService;
import ru.dankoy.kafkamessageconsumer.core.service.telegrambot.TelegramBotService;

@ExtendWith(MockitoExtension.class)
class CoubMessageConsumerImplTest implements MessageMaker {

  @Mock private TelegramBotService telegramBotService;

  @Mock private SentCoubsRegistryService sentCoubsRegistryService;

  @Mock private SubscriptionService subscriptionService;

  private CoubMessageConsumerImpl coubMessageConsumer;

  @BeforeEach
  public void setUp() {

    coubMessageConsumer =
        new CoubMessageConsumerImpl(
            telegramBotService::sendChannelMessage,
            sentCoubsRegistryService::create,
            subscriptionService::updatePermalink);
  }

  @DisplayName("accept single message expects correct behaviour")
  @Test
  void acceptTest_singleMessage() {

    var message = makeMessage();

    coubMessageConsumer.accept(message);

    verify(telegramBotService, times(1)).sendChannelMessage(message);
    verify(sentCoubsRegistryService, times(1)).create(message);
    verify(subscriptionService, times(1)).updatePermalink(message);
  }

  @DisplayName("accept batch expects correct behaviour")
  @Test
  void acceptBatchTest_batch() {

    var messages = List.of(makeMessage());

    coubMessageConsumer.accept(messages);

    verify(telegramBotService, times(1)).sendChannelMessage(messages.getFirst());
    verify(sentCoubsRegistryService, times(1)).create(messages.getFirst());
    verify(subscriptionService, times(1)).updatePermalink(messages.getFirst());
  }
}
