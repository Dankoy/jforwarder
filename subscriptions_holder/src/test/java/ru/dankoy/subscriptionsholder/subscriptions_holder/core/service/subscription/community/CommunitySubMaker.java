package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.subscription.community;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.CommunitySub;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community.Community;

public interface CommunitySubMaker {

  default List<CommunitySub> makeCorrectCommunitySubs(Community community, Chat chat) {

    var lastPermalink = "lp";

    CommunitySub communitySub =
        CommunitySub.builder()
            .id(0)
            .section(community.getSections().iterator().next())
            .community(community)
            .chat(chat)
            .chatUuid(UUID.randomUUID())
            .lastPermalink(lastPermalink)
            .build();

    return Stream.of(communitySub).toList();
  }
}
