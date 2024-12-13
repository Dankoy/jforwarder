package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Subscription;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.SubscriptionRepository;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

  private final SubscriptionRepository subscriptionRepository;

  @Override
  public Subscription findById(long id) {

    var optional = subscriptionRepository.findById(id);

    return checkOptional(optional, id);
  }

  @Transactional
  @Override
  public Subscription updatePermalink(Subscription subscription) {

    var optionalSub = subscriptionRepository.findForUpdateById(subscription.getId());
    var sub = checkOptional(optionalSub, subscription.getId());

    sub.setLastPermalink(subscription.getLastPermalink());
    return subscriptionRepository.save(sub);
  }

  private Subscription checkOptional(Optional<Subscription> optional, long id) {

    return optional.orElseThrow(
        () -> new ResourceNotFoundException(String.format("Subscription not found: %s", id)));
  }
}
