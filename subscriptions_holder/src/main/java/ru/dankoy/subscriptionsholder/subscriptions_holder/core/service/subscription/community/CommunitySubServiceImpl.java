package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.subscription.community;

import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.CommunitySub;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community.Section;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceConflictException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.subscription.community.CommunitySubRepository;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.community.CommunityService;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.section.SectionService;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.telegramchat.TelegramChatService;

@Service
@RequiredArgsConstructor
public class CommunitySubServiceImpl implements CommunitySubService {

  private final CommunitySubRepository communitySubRepository;
  private final CommunityService communityService;
  private final TelegramChatService telegramChatService;
  private final SectionService sectionService;

  @Override
  public Page<CommunitySub> findAll(Pageable pageable) {
    return communitySubRepository.findAll(pageable);
  }

  @Override
  public Page<CommunitySub> findAllByChatActive(boolean isActive, Pageable pageable) {
    return communitySubRepository.findAllByChatActive(isActive, pageable);
  }

  @Override
  public List<CommunitySub> getAllByChatChatId(long telegramChatId) {
    return communitySubRepository.getAllByChatChatId(telegramChatId);
  }

  @Override
  public List<CommunitySub> getAllByChatChatIdAndMessageThreadId(
      long telegramChatId, Integer messageThreadId) {
    return communitySubRepository.getAllByChatChatIdAndChatMessageThreadId(
        telegramChatId, messageThreadId);
  }

  @Override
  public Optional<CommunitySub> getByCommunityNameSectionNameChatId(
      String communityName, String sectionName, long chatId) {

    return communitySubRepository.getByChatChatIdAndCommunityNameAndSectionName(
        chatId, communityName, sectionName);
  }

  @Override
  public Optional<CommunitySub> getByCommunityNameSectionNameChatIdMessageThreadId(
      String communityName, String sectionName, long chatId, Integer messageThreadId) {

    return communitySubRepository
        .getByChatChatIdAndChatMessageThreadIdAndCommunityNameAndSectionName(
            chatId, messageThreadId, communityName, sectionName);
  }

  @Transactional
  @Override
  public CommunitySub subscribeChatToCommunity(CommunitySub communitySubscription) {

    var foundSubscriptionOptional =
        communitySubRepository.getByChatChatIdAndChatMessageThreadIdAndCommunityNameAndSectionName(
            communitySubscription.getChat().getChatId(),
            communitySubscription.getChat().getMessageThreadId(),
            communitySubscription.getCommunity().getName(),
            communitySubscription.getSection().getName());

    // if exists - exception
    foundSubscriptionOptional.ifPresent(
        s -> {
          throw new ResourceConflictException(
              String.format(
                  "Subscription already exists for community '%s-%s' and chat" + " '%d'",
                  s.getCommunity().getName(), s.getSection().getName(), s.getChat().getChatId()));
        });

    Set<Section> sectionsToFind = Collections.singleton(communitySubscription.getSection());

    var foundSectionOptional =
        sectionService.getSectionByName(communitySubscription.getSection().getName());

    var foundSection =
        foundSectionOptional.orElseThrow(
            () ->
                new ResourceNotFoundException(
                    String.format(
                        "Section not found - %s", communitySubscription.getSection().getName())));

    // Получаем коммунити
    var foundCommunity =
        communityService.getByNameAndSectionIn(
            communitySubscription.getCommunity().getName(), sectionsToFind);

    var foundTelegramChat =
        telegramChatService.getByTelegramChatIdAndMessageThreadId(
            communitySubscription.getChat().getChatId(),
            communitySubscription.getChat().getMessageThreadId());

    if (foundTelegramChat.isPresent()) {

      var chat = foundTelegramChat.get();
      // use it

      // if subscription not exist - create new subscription for chat
      var newSubscription =
          CommunitySub.builder()
              .id(0)
              .community(foundCommunity)
              .section(foundSection)
              .chat(chat)
              .chatUuid(communitySubscription.getChatUuid())
              .lastPermalink(null)
              .build();

      return communitySubRepository.save(newSubscription);

    } else {

      // if chat not exists - create it and create subscription

      var createdChat = telegramChatService.save(communitySubscription.getChat());

      var newSubscription =
          CommunitySub.builder()
              .id(0)
              .community(foundCommunity)
              .section(foundSection)
              .chat(createdChat)
              .chatUuid(communitySubscription.getChatUuid())
              .lastPermalink(null)
              .build();

      return communitySubRepository.save(newSubscription);
    }
  }

  @Transactional
  @Override
  public void unsubscribeChatFromCommunity(CommunitySub communitySubscription) {

    var subscriptionFoundOptional =
        communitySubRepository.getByChatChatIdAndChatMessageThreadIdAndCommunityNameAndSectionName(
            communitySubscription.getChat().getChatId(),
            communitySubscription.getChat().getMessageThreadId(),
            communitySubscription.getCommunity().getName(),
            communitySubscription.getSection().getName());

    subscriptionFoundOptional.ifPresent(communitySubRepository::delete);
  }

  @Override
  public Page<CommunitySub> findAllByChatsUUID(List<UUID> chatUuids, Pageable pageable) {
    // List<String> uuids = chatUuids.stream().map(UUID::toString).toList();
    return communitySubRepository.findAllBychatUuidIsIn(chatUuids, pageable);
  }
}
