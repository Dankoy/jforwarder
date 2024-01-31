package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community.Section;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.CommunitySubscription;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceConflictException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.CommunitySubscriptionRepository;


/**
 * @deprecated in favor of {@link CommunitySubServiceImpl}
 */
@Deprecated(since = "2024-01-27")
@RequiredArgsConstructor
@Service
public class SubscriptionServiceImpl implements SubscriptionService {

  private final CommunitySubscriptionRepository communitySubscriptionRepository;
  private final CommunityService communityService;
  private final TelegramChatService telegramChatService;
  private final SectionService sectionService;

  @Override
  public Page<CommunitySubscription> getAll(Pageable pageable) {
    return communitySubscriptionRepository.findAll(pageable);
  }

  @Override
  public Page<CommunitySubscription> getAllWithActiveChats(boolean active, Pageable pageable) {
    return communitySubscriptionRepository.findAllByChatActive(active, pageable);
  }

  @Override
  public List<CommunitySubscription> getAllByCommunityName(String communityName) {
    return communitySubscriptionRepository.getAllByCommunityName(communityName);
  }

  @Override
  public List<CommunitySubscription> getAllByChatId(long chatId) {
    return communitySubscriptionRepository.getAllByChatChatId(chatId);
  }

  @Override
  public Optional<CommunitySubscription> getByCommunityNameSectionNameChatId(String communityName,
      String sectionName, long chatId) {

    return communitySubscriptionRepository
        .getByChatChatIdAndCommunityNameAndSectionName(
            chatId, communityName, sectionName);

  }

  @Transactional
  @Override
  public CommunitySubscription subscribeChatToCommunity(CommunitySubscription communitySubscription) {

    var foundSubscriptionOptional = communitySubscriptionRepository
        .getByChatChatIdAndCommunityNameAndSectionName(
            communitySubscription.getChat().getChatId(), communitySubscription.getCommunity().getName(),
            communitySubscription.getSection().getName());

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

    Set<Section> sectionsToFind = Collections.singleton(communitySubscription.getSection());

    var foundSectionOptional = sectionService.getSectionByName(communitySubscription.getSection().getName());

    var foundSection = foundSectionOptional.orElseThrow(
        () -> new ResourceNotFoundException(
            String.format("Section not found - %s", communitySubscription.getSection().getName())));

    // Получаем коммунити
    var foundCommunity = communityService.getByNameAndSectionIn(
        communitySubscription.getCommunity().getName(),
        sectionsToFind);

    var foundTelegramChat = telegramChatService.getByTelegramChatId(
        communitySubscription.getChat().getChatId()
    );

    if (foundTelegramChat.isPresent()) {

      var chat = foundTelegramChat.get();
      // use it

      // if subscription not exist - create new subscription for chat
      var newSubscription = new CommunitySubscription(
          0, foundCommunity, foundSection, chat, null
      );

      return communitySubscriptionRepository.save(newSubscription);

    } else {

      // if chat not exists - create it and create subscription

      var createdChat = telegramChatService.save(communitySubscription.getChat());

      var newSubscription = new CommunitySubscription(
          0, foundCommunity, foundSection, createdChat, null
      );

      return communitySubscriptionRepository.save(newSubscription);

    }
  }

  @Transactional
  @Override
  public void unsubscribeChatFromCommunity(CommunitySubscription communitySubscription) {

    var subscriptionFoundOptional = communitySubscriptionRepository
        .getByChatChatIdAndCommunityNameAndSectionName(
            communitySubscription.getChat().getChatId(),
            communitySubscription.getCommunity().getName(),
            communitySubscription.getSection().getName());

    subscriptionFoundOptional.ifPresent(communitySubscriptionRepository::delete);

  }

  @Transactional
  @Override
  public CommunitySubscription updateLastPermalink(CommunitySubscription communitySubscription) {

    var foundSubscriptionOptional = communitySubscriptionRepository
        .getByChatChatIdAndCommunityNameAndSectionName(
            communitySubscription.getChat().getChatId(),
            communitySubscription.getCommunity().getName(),
            communitySubscription.getSection().getName());

    var found = foundSubscriptionOptional.orElseThrow(() -> new ResourceNotFoundException(
            String.format("Subscription not found for community '%s-%s' and chat '%d'",
                communitySubscription.getCommunity().getName(),
                communitySubscription.getSection().getName(),
                communitySubscription.getChat().getChatId())
        )
    );

    found.setLastPermalink(communitySubscription.getLastPermalink());
    return communitySubscriptionRepository.save(found);

  }

}
