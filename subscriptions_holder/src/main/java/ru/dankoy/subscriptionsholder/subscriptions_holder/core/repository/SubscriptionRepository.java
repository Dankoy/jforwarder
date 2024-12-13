package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
  Optional<Subscription> findForUpdateById(long id);
}
