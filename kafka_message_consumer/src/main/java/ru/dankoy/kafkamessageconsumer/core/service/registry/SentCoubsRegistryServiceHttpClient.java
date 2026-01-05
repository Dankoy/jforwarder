package ru.dankoy.kafkamessageconsumer.core.service.registry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CoubMessage;
import ru.dankoy.kafkamessageconsumer.core.domain.registry.SentCoubsRegistry;
import ru.dankoy.kafkamessageconsumer.core.httpservice.subscriptionsholder.SubscriptionsHolderHttpService;
import ru.dankoy.kafkamessageconsumer.core.service.converter.MessageConverter;

@Service("sentCoubsRegistryServiceHttpClient")
@RequiredArgsConstructor
public class SentCoubsRegistryServiceHttpClient implements SentCoubsRegistryService {

  private final SubscriptionsHolderHttpService subscriptionHttpService;

  private final MessageConverter messageConverter;

  @Override
  public SentCoubsRegistry create(CoubMessage coubMessage) {
    var subscription = messageConverter.convertToRegistry(coubMessage);
    return subscriptionHttpService.createRegistryEntry(subscription);
  }
}
