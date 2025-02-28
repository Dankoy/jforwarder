package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Subscription;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.SubscriptionRepository;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

  private final SubscriptionRepository subscriptionRepository;

  @Override
  public List<Subscription> findByChats(List<Chat> chats) {
    return subscriptionRepository.findByChatIsIn(chats);
  }

  @Override
  public Page<Subscription> findAll(Pageable pageable) {
    return subscriptionRepository.findAll(pageable);
  }

  @Override
  public Page<Subscription> findAllByChatsUUID(List<UUID> chatUuids, Pageable pageable) {

    var list = chatUuids.stream().map(UUID::toString).toList();

    return subscriptionRepository.findAllBychatUuidIsIn(list, pageable);
  }

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
