package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.scope;

import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Scope;

public interface ScopeService {

  Scope getByName(String name);
}
