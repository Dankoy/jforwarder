package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.registry.SentCoubsRegistry;

public interface SentCoubsRegistryService {

    Page<SentCoubsRegistry> findAll(Pageable pageable);

    Page<SentCoubsRegistry> getAllBySubscriptionId(long subscriptionId, Pageable pageable);

    Page<SentCoubsRegistry> getAllBySubscriptionIdAndDateTimeAfter(
            long subscriptionId, LocalDateTime dateTime, Pageable pageable);

    SentCoubsRegistry create(SentCoubsRegistry sentCoubsRegistry);

    void deleteById(long id);
}
