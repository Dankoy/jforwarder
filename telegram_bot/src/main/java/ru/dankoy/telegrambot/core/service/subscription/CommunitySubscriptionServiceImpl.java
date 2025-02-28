package ru.dankoy.telegrambot.core.service.subscription;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.telegrambot.core.domain.Chat;
import ru.dankoy.telegrambot.core.domain.subscription.community.Community;
import ru.dankoy.telegrambot.core.domain.subscription.community.CommunitySubscription;
import ru.dankoy.telegrambot.core.domain.subscription.community.Section;
import ru.dankoy.telegrambot.core.exceptions.ExceptionObjectType;
import ru.dankoy.telegrambot.core.exceptions.NotFoundException;
import ru.dankoy.telegrambot.core.feign.subscriptionsholder.SubscriptionsHolderFeign;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatService;
import ru.dankoy.telegrambot.core.service.community.CommunityService;

@Service
@RequiredArgsConstructor
public class CommunitySubscriptionServiceImpl implements CommunitySubscriptionService {

  private final SubscriptionsHolderFeign subscriptionsHolderFeign;

  private final CommunityService communityService;

  private final TelegramChatService telegramChatService;

  /**
   * @deprecated for topics support via messageThreadId
   */
  @Deprecated(since = "2024-04-05", forRemoval = true)
  public List<CommunitySubscription> getSubscriptionsByChatId(long chatId) {

    return subscriptionsHolderFeign.getAllSubscriptionsByChatId(chatId);
  }

  /**
   * @deprecated chat is in separate microservice and db
   */
  @Deprecated(since = "2025-02-28", forRemoval = false)
  public List<CommunitySubscription> getSubscriptionsByChatIdAndMessageThreadId(
      long chatId, Integer messageThreadId) {

    return subscriptionsHolderFeign.getAllSubscriptionsByChatIdAndMessageThreadId(
        chatId, messageThreadId);
  }

  @Override
  public List<CommunitySubscription> getSubsByChatIdAndMessageThreadId(
      long chatId, Integer messageThreadId) {

    var chat = telegramChatService.getChatByIdAndMessageThreadId(chatId, messageThreadId);

    return subscriptionsHolderFeign.getAllCommunitySubscriptionsByChatUuid(chat.getId());
  }

  @Override
  public CommunitySubscription subscribe(
      String communityName, String sectionName, long chatId, Integer messageThreadId) {

    // 1. find community
    var optionalCommunity = communityService.getByName(communityName);

    var community =
        optionalCommunity.orElseThrow(
            () ->
                new NotFoundException(
                    ExceptionObjectType.COMMUNITY,
                    communityName,
                    String.format("Community with name '%s' not found", communityName)));

    // 2. find section
    var optionalSection =
        community.getSections().stream().filter(s -> s.getName().equals(sectionName)).findFirst();

    var section =
        optionalSection.orElseThrow(
            () ->
                new NotFoundException(
                    ExceptionObjectType.SECTION,
                    sectionName,
                    String.format("Section with name '%s' not found", sectionName)));

    // get chat from separate microservice
    var chat = telegramChatService.getChatByIdAndMessageThreadId(chatId, messageThreadId);

    var subscription =
        (CommunitySubscription)
            CommunitySubscription.builder()
                .id(0)
                .community(community)
                .chat(new Chat(chatId, messageThreadId))
                .chatUuid(chat.getId())
                .section(section)
                .build();

    return subscriptionsHolderFeign.subscribe(subscription);
  }

  @Override
  public void unsubscribe(
      String communityName, String sectionName, long chatId, Integer messageThreadId) {

    // get chat from separate microservice
    var chat = telegramChatService.getChatByIdAndMessageThreadId(chatId, messageThreadId);

    var subscription =
        (CommunitySubscription)
            CommunitySubscription.builder()
                .id(0)
                .community(new Community(communityName))
                .chat(new Chat(chatId, messageThreadId))
                .chatUuid(chat.getId())
                .section(new Section(sectionName))
                .build();

    subscriptionsHolderFeign.unsubscribe(subscription);
  }
}
