package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.subscription;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

  @EntityGraph(value = "subscription-full")
  Page<Subscription> findAll(Pageable pageable);

  List<Subscription> findByChatIsIn(List<Chat> chats);

  @EntityGraph(value = "subscription-parent-only", type = EntityGraphType.FETCH)
  Page<Subscription> findAllBychatUuidIsIn(
      @Param(value = "chatUuid") List<UUID> chatUuid, Pageable pageable);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
  Optional<Subscription> findForUpdateById(long id);
}
