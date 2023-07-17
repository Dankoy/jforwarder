package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.CommunityTelegramChatPK;
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

  @Override
  public List<Subscription> getAll() {
    return subscriptionRepository.findAll();
  }

  @Override
  public List<Subscription> getAllByCommunityName(String communityName) {
    return subscriptionRepository.getAllByCommunityChatCommunityName(communityName);
  }

  @Override
  public List<Subscription> getAllByChatId(long chatId) {
    return subscriptionRepository.getAllByCommunityChatTelegramChatId(chatId);
  }

  @Override
  public Optional<Subscription> getByCommunityNameSectionNameChatId(String communityName,
      String sectionName, long chatId) {

    return subscriptionRepository
        .getSubscriptionByCommunityChatTelegramChatChatIdAndCommunityChatCommunityNameAndCommunityChatCommunitySectionName(
            chatId, communityName, sectionName);

  }

  @Transactional
  @Override
  public Subscription subscribeChatToCommunity(Subscription subscription) {

    // Получаем коммунити
    var foundCommunity = communityService.getByNameAndSectionName(
        subscription.getCommunityChat().getCommunity().getName(),
        subscription.getCommunityChat().getCommunity().getSection().getName());

    var foundTelegramChat = telegramChatService.getByTelegramChatId(
        subscription.getCommunityChat().getTelegramChat().getChatId()
    );

    if (foundTelegramChat.isPresent()) {

      var chat = foundTelegramChat.get();
      // use it

      var foundSubscriptionOptional = subscriptionRepository
          .getSubscriptionByCommunityChatTelegramChatChatIdAndCommunityChatCommunityNameAndCommunityChatCommunitySectionName(
              chat.getChatId(), foundCommunity.getName(), foundCommunity.getSection().getName());

      // if exists - exception
      foundSubscriptionOptional.ifPresent(s -> {
            throw new ResourceConflictException(
                String.format("Subscription already exists for community '%s-%s' and chat '%d'",
                    s.getCommunityChat().getCommunity().getName(),
                    s.getCommunityChat().getCommunity().getSection().getName(),
                    s.getCommunityChat().getTelegramChat().getChatId())
            );
          }
      );

      // if not exist - create new subscription for chat
      var newSubscription = new Subscription(
          new CommunityTelegramChatPK(foundCommunity, chat), null
      );

      return subscriptionRepository.save(newSubscription);

    } else {

      // if chat not exists - create it and create subscription

      var createdChat = telegramChatService.save(subscription.getCommunityChat().getTelegramChat());

      var newSubscription = new Subscription(
          new CommunityTelegramChatPK(foundCommunity, createdChat), null
      );

      return subscriptionRepository.save(newSubscription);

    }
  }

  @Transactional
  @Override
  public void unsubscribeChatFromCommunity(Subscription subscription) {

    var subscriptionFoundOptional = subscriptionRepository
        .getSubscriptionByCommunityChatTelegramChatChatIdAndCommunityChatCommunityNameAndCommunityChatCommunitySectionName(
            subscription.getCommunityChat().getTelegramChat().getChatId(),
            subscription.getCommunityChat().getCommunity().getName(),
            subscription.getCommunityChat().getCommunity().getSection().getName());

    subscriptionFoundOptional.ifPresent(subscriptionRepository::delete);

  }

  @Transactional
  @Override
  public Subscription updateLastPermalink(Subscription subscription) {

    var foundTelegramChat = telegramChatService.getByTelegramChatId(
        subscription.getCommunityChat().getTelegramChat().getChatId()
    );

    if (foundTelegramChat.isPresent()) {

      var chat = foundTelegramChat.get();

      var subscriptionFoundOptional = subscriptionRepository
          .getSubscriptionByCommunityChatTelegramChatChatIdAndCommunityChatCommunityNameAndCommunityChatCommunitySectionName(
              chat.getChatId(),
              subscription.getCommunityChat().getCommunity().getName(),
              subscription.getCommunityChat().getCommunity().getSection().getName());

      var subscriptionFound = subscriptionFoundOptional.orElseThrow(
          () -> new ResourceNotFoundException(
              String.format("Subscription not found for community '%s-%s' and chat '%d'",
                  subscription.getCommunityChat().getCommunity().getName(),
                  subscription.getCommunityChat().getCommunity().getSection().getName(),
                  subscription.getCommunityChat().getTelegramChat().getChatId())
          ));

      subscriptionFound.setLastPermalink(subscription.getLastPermalink());
      return subscriptionRepository.save(subscriptionFound);

    } else {

      throw new ResourceNotFoundException(
          String.format("Chat not found - %d",
              subscription.getCommunityChat().getTelegramChat().getChatId()));

    }

  }

}
