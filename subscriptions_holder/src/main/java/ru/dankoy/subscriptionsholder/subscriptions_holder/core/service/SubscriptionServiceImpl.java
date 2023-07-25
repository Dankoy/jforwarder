package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Section;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Subscription;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceConflictException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.SubscriptionRepository;


@RequiredArgsConstructor
@Service
public class SubscriptionServiceImpl implements SubscriptionService {

  private final SubscriptionRepository subscriptionRepository;
  private final CommunityService communityService;
  private final TelegramChatService telegramChatService;
  private final SectionService sectionService;

  @Override
  public List<Subscription> getAll() {
    return subscriptionRepository.findAll();
  }

  @Override
  public List<Subscription> getAllWithActiveChats(boolean active) {
    return subscriptionRepository.findAllWithActiveChats(active);
  }

  @Override
  public List<Subscription> getAllByCommunityName(String communityName) {
    return subscriptionRepository.getAllByCommunityName(communityName);
  }

  @Override
  public List<Subscription> getAllByChatId(long chatId) {
    return subscriptionRepository.getAllByChatChatId(chatId);
  }

  @Override
  public Optional<Subscription> getByCommunityNameSectionNameChatId(String communityName,
      String sectionName, long chatId) {

    return subscriptionRepository
        .getByChatChatIdAndCommunityNameAndSectionName(
            chatId, communityName, sectionName);

  }

  @Transactional
  @Override
  public Subscription subscribeChatToCommunity(Subscription subscription) {

    var foundSubscriptionOptional = subscriptionRepository
        .getByChatChatIdAndCommunityNameAndSectionName(
            subscription.getChat().getChatId(), subscription.getCommunity().getName(),
            subscription.getSection().getName());

    // if exists - exception
    foundSubscriptionOptional.ifPresent(s -> {
          throw new ResourceConflictException(
              String.format("Subscription already exists for community '%s-%s' and chat '%d'",
                  s.getCommunity().getName(),
                  s.getSection().getName(),
                  s.getChat().getChatId())
          );
        }
    );

    Set<Section> sectionsToFind = Collections.singleton(subscription.getSection());

    var foundSectionOptional = sectionService.getSectionByName(subscription.getSection().getName());

    var foundSection = foundSectionOptional.orElseThrow(
        () -> new ResourceNotFoundException(
            String.format("Section not found - %s", subscription.getSection().getName())));

    // Получаем коммунити
    var foundCommunity = communityService.getByNameAndSectionIn(
        subscription.getCommunity().getName(),
        sectionsToFind);

    var foundTelegramChat = telegramChatService.getByTelegramChatId(
        subscription.getChat().getChatId()
    );

    if (foundTelegramChat.isPresent()) {

      var chat = foundTelegramChat.get();
      // use it

      // if subscription not exist - create new subscription for chat
      var newSubscription = new Subscription(
          0, foundCommunity, foundSection, chat, null
      );

      return subscriptionRepository.save(newSubscription);

    } else {

      // if chat not exists - create it and create subscription

      var createdChat = telegramChatService.save(subscription.getChat());

      var newSubscription = new Subscription(
          0, foundCommunity, foundSection, createdChat, null
      );

      return subscriptionRepository.save(newSubscription);

    }
  }

  @Transactional
  @Override
  public void unsubscribeChatFromCommunity(Subscription subscription) {

    var subscriptionFoundOptional = subscriptionRepository
        .getByChatChatIdAndCommunityNameAndSectionName(
            subscription.getChat().getChatId(),
            subscription.getCommunity().getName(),
            subscription.getSection().getName());

    subscriptionFoundOptional.ifPresent(subscriptionRepository::delete);

  }

  @Transactional
  @Override
  public Subscription updateLastPermalink(Subscription subscription) {

    var foundSubscriptionOptional = subscriptionRepository
        .getByChatChatIdAndCommunityNameAndSectionName(
            subscription.getChat().getChatId(),
            subscription.getCommunity().getName(),
            subscription.getSection().getName());

    var found = foundSubscriptionOptional.orElseThrow(() -> new ResourceNotFoundException(
            String.format("Subscription not found for community '%s-%s' and chat '%d'",
                subscription.getCommunity().getName(),
                subscription.getSection().getName(),
                subscription.getChat().getChatId())
        )
    );

    found.setLastPermalink(subscription.getLastPermalink());
    return subscriptionRepository.save(found);

  }

}
