package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Scope;

public interface ScopeRepository extends JpaRepository<Scope, Long> {

  Optional<Scope> findByName(String name);

}
