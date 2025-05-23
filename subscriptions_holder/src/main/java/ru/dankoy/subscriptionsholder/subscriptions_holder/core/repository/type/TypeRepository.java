package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.type;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Type;

public interface TypeRepository extends JpaRepository<Type, Long> {

  Optional<Type> findByName(String name);
}
