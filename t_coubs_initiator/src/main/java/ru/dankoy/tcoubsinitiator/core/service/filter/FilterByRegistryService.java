package ru.dankoy.tcoubsinitiator.core.service.filter;

import java.util.List;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.subscription.Subscription;

public interface FilterByRegistryService {

  void filterByRegistry(List<? extends Subscription> subscriptions);

}
