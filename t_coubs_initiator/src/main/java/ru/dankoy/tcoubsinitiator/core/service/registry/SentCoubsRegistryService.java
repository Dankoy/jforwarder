package ru.dankoy.tcoubsinitiator.core.service.registry;

import java.time.LocalDateTime;
import java.util.Set;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.registry.SentCoubsRegistry;

public interface SentCoubsRegistryService {

  Set<SentCoubsRegistry> getAllBySubscriptionIdAndDateTimeAfter(
      long subscriptionId, LocalDateTime dateTime);
}
