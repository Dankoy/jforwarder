package ru.dankoy.kafkamessageconsumer.core.service.converter;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CoubMessage;
import ru.dankoy.kafkamessageconsumer.core.domain.registry.SentCoubsRegistry;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Subscription;

@Component
public class MessageConverterImpl implements MessageConverter {

  @Override
  public SentCoubsRegistry convertToRegistry(CoubMessage message) {
    return SentCoubsRegistry.builder()
        .subscription(Subscription.builder().id(message.getId()).build())
        .coubPermalink(message.getCoub().getPermalink())
        .dateTime(LocalDateTime.now())
        .build();
  }

  @Override
  public Subscription convert(CoubMessage message) {

    return Subscription.builder()
        .id(message.getId())
        .chat(message.getChat())
        .lastPermalink(message.getCoub().getPermalink())
        .build();
  }
}
