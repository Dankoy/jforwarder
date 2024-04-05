package ru.dankoy.telegrambot.core.service.subscription;

import java.util.List;
import java.util.stream.Stream;
import ru.dankoy.telegrambot.core.domain.Chat;
import ru.dankoy.telegrambot.core.domain.subscription.community.Community;
import ru.dankoy.telegrambot.core.domain.subscription.community.CommunitySubscription;

public interface CommunitySubMaker {

  default List<CommunitySubscription> makeCorrectCommunitySubs(Community community, Chat chat) {

    var lastPermalink = "lp";

    CommunitySubscription communitySub =
        CommunitySubscription.builder()
            .id(0)
            .section(community.getSections().iterator().next())
            .community(community)
            .chat(chat)
            .lastPermalink(lastPermalink)
            .build();

    return Stream.of(communitySub).toList();
  }
}
