package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.scope;

import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Scope;

public interface ScopeMaker {

  default Scope makeCorrectScope() {

    return new Scope(1L, "all");
  }
}
