package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.subscription;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Subscription;

public interface SubscriptionService {

  List<Subscription> findByChats(List<Chat> chats);

  Page<Subscription> findAll(Pageable pageable);

  Page<Subscription> findAllByChatsUUID(List<UUID> chatUuids, Pageable pageable);

  Subscription findById(long id);

  Subscription updatePermalink(Subscription subscription);
}
