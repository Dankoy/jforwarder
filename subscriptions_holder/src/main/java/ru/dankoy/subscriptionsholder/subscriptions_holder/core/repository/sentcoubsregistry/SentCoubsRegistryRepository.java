package ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.sentcoubsregistry;

import feign.Param;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.registry.SentCoubsRegistry;

public interface SentCoubsRegistryRepository extends JpaRepository<SentCoubsRegistry, Long> {

  @Override
  @EntityGraph(value = "sent-coubs-registry-full", type = EntityGraphType.LOAD)
  Page<SentCoubsRegistry> findAll(Pageable pageable);

  @EntityGraph(value = "sent-coubs-registry-full", type = EntityGraphType.LOAD)
  Page<SentCoubsRegistry> getAllBySubscriptionId(
      @Param("subscriptionId") long subscriptionId, Pageable pageable);

  @EntityGraph(
      value = "sent-coubs-registry-with-partial-subscription",
      type = EntityGraphType.FETCH)
  Page<SentCoubsRegistry> getAllBySubscriptionIdAndDateTimeAfter(
      @Param("subscriptionId") long subscriptionId,
      @Param("dateTime") LocalDateTime dateTime,
      Pageable pageable);

  // Optional no more. Read how getReferenceById works.
  SentCoubsRegistry getReferenceById(long id);
}
