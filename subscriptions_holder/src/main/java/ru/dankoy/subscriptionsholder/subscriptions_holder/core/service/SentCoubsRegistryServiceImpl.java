package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.registry.SentCoubsRegistry;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.SentCoubsRegistryRepository;

@Service
@RequiredArgsConstructor
public class SentCoubsRegistryServiceImpl implements SentCoubsRegistryService {

  private final SentCoubsRegistryRepository sentCoubsRegistryRepository;

  @Override
  public Page<SentCoubsRegistry> findAll(Pageable pageable) {
    return sentCoubsRegistryRepository.findAll(pageable);
  }

  @Override
  public Page<SentCoubsRegistry> getAllBySubscriptionId(long subscriptionId, Pageable pageable) {
    return sentCoubsRegistryRepository.getAllBySubscriptionId(subscriptionId, pageable);
  }

  @Override
  public Page<SentCoubsRegistry> getAllBySubscriptionIdAndDateTimeAfter(
      long subscriptionId, LocalDateTime dateTime, Pageable pageable) {
    return sentCoubsRegistryRepository.getAllBySubscriptionIdAndDateTimeAfter(subscriptionId,
        dateTime, pageable);
  }

  @Transactional
  @Override
  public SentCoubsRegistry create(SentCoubsRegistry sentCoubsRegistry) {
    return sentCoubsRegistryRepository.save(sentCoubsRegistry);
  }

  @Transactional
  @Override
  public void deleteById(long id) {
    var optional = sentCoubsRegistryRepository.getById(id);
    optional.ifPresent(sentCoubsRegistryRepository::delete);
  }
}
