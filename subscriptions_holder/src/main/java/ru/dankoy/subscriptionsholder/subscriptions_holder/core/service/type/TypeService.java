package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.type;

import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Type;

public interface TypeService {

  Type getByName(String name);
}
