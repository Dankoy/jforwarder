package ru.dankoy.kafkamessageconsumer.core.service.converter;

import ru.dankoy.kafkamessageconsumer.core.domain.message.CoubMessage;
import ru.dankoy.kafkamessageconsumer.core.domain.registry.SentCoubsRegistry;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Subscription;

public interface MessageConverter {

  SentCoubsRegistry convertToRegistry(CoubMessage message);

  Subscription convert(CoubMessage message);
}
