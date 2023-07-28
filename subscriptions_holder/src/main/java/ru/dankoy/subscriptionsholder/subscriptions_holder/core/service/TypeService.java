package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.tag.Type;

public interface TypeService {

  Type getByName(String name);

}
