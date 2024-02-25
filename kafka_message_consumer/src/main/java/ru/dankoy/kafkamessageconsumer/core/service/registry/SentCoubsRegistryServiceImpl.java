package ru.dankoy.kafkamessageconsumer.core.service.registry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CoubMessage;
import ru.dankoy.kafkamessageconsumer.core.domain.registry.SentCoubsRegistry;
import ru.dankoy.kafkamessageconsumer.core.feign.subscriptionsholder.SubscriptionFeign;
import ru.dankoy.kafkamessageconsumer.core.service.converter.MessageConverter;

@Service
@RequiredArgsConstructor
public class SentCoubsRegistryServiceImpl implements SentCoubsRegistryService {

  private final SubscriptionFeign subscriptionFeign;

  private final MessageConverter messageConverter;

  @Override
  public SentCoubsRegistry create(CoubMessage coubMessage) {
    var subscription = messageConverter.convertToRegistry(coubMessage);
    return subscriptionFeign.createRegistryEntry(subscription);
  }
}
