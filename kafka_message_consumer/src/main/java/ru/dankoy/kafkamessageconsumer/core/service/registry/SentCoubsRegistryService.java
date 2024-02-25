package ru.dankoy.kafkamessageconsumer.core.service.registry;

import ru.dankoy.kafkamessageconsumer.core.domain.message.CoubMessage;
import ru.dankoy.kafkamessageconsumer.core.domain.registry.SentCoubsRegistry;

public interface SentCoubsRegistryService {

  SentCoubsRegistry create(CoubMessage coubMessage);
}
