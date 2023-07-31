package ru.dankoy.telegrambot.core.service.subscription;


import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.telegrambot.core.domain.subscription.Chat;
import ru.dankoy.telegrambot.core.domain.subscription.Community;
import ru.dankoy.telegrambot.core.domain.subscription.Section;
import ru.dankoy.telegrambot.core.domain.subscription.CommunitySubscription;
import ru.dankoy.telegrambot.core.feign.subscriptionsholder.SubscriptionsHolderFeign;

@Service
@RequiredArgsConstructor
public class CommunitySubscriptionServiceImpl implements CommunitySubscriptionService {

  private final SubscriptionsHolderFeign subscriptionsHolderFeign;


  public List<CommunitySubscription> getSubscriptionsByChatId(long chatId) {

    return subscriptionsHolderFeign.getAllSubscriptionsByChatId(chatId);

  }

  @Override
  public CommunitySubscription subscribe(String communityName, String sectionName, long chatId) {

    var subscription = new CommunitySubscription(
        0,
        new Community(
            communityName
        ),
        new Chat(
            chatId
        ),
        new Section(
            sectionName
        ),
        null,
        new ArrayList<>()
    );

    return subscriptionsHolderFeign.subscribe(subscription);
  }

  @Override
  public void unsubscribe(String communityName, String sectionName, long chatId) {

    var subscription = new CommunitySubscription(
        0,
        new Community(
            communityName
        ),
        new Chat(
            chatId
        ),
        new Section(
            sectionName
        ),
        null,
        new ArrayList<>()
    );

    subscriptionsHolderFeign.unsubscribe(subscription);
  }


}
