package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.tag.Scope;

public interface ScopeService {

  Scope getByName(String name);

}
