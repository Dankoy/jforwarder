package ru.dankoy.telegrambot.core.service.subscription;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.telegrambot.core.domain.subscription.Chat;
import ru.dankoy.telegrambot.core.domain.subscription.Community;
import ru.dankoy.telegrambot.core.domain.subscription.CommunitySubscription;
import ru.dankoy.telegrambot.core.domain.subscription.Section;
import ru.dankoy.telegrambot.core.exceptions.ExceptionObjectType;
import ru.dankoy.telegrambot.core.exceptions.NotFoundException;
import ru.dankoy.telegrambot.core.feign.subscriptionsholder.SubscriptionsHolderFeign;
import ru.dankoy.telegrambot.core.service.community.CommunityService;

@Service
@RequiredArgsConstructor
public class CommunitySubscriptionServiceImpl implements CommunitySubscriptionService {

  private final SubscriptionsHolderFeign subscriptionsHolderFeign;

  private final CommunityService communityService;

  public List<CommunitySubscription> getSubscriptionsByChatId(long chatId) {

    return subscriptionsHolderFeign.getAllSubscriptionsByChatId(chatId);
  }

  @Override
  public CommunitySubscription subscribe(String communityName, String sectionName, long chatId) {

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

    var subscription =
        new CommunitySubscription(0, community, new Chat(chatId), section, null, new ArrayList<>());

    return subscriptionsHolderFeign.subscribe(subscription);
  }

  @Override
  public void unsubscribe(String communityName, String sectionName, long chatId) {

    var subscription =
        new CommunitySubscription(
            0,
            new Community(communityName),
            new Chat(chatId),
            new Section(sectionName),
            null,
            new ArrayList<>());

    subscriptionsHolderFeign.unsubscribe(subscription);
  }
}
