package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.subscription.tag;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.TagSub;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.tag.Tag;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.order.OrderMaker;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.scope.ScopeMaker;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.type.TypeMaker;

public interface TagSubMaker extends ScopeMaker, TypeMaker, OrderMaker {

  default List<TagSub> makeTagSubs(Tag tag, Chat chat) {

    var value = "popular";
    var orderType = "tag";

    var lastPermalink = "lp";

    var scope = makeCorrectScope(); // from migration
    var type = makeCorrectType(); // from migration
    var order = findCorrectByValueAndSubscriptionType(value, orderType);

    TagSub channelSub =
        TagSub.builder()
            .id(0)
            .tag(tag)
            .scope(scope)
            .type(type)
            .order(order)
            .chat(chat)
            .chatUuid(UUID.randomUUID())
            .lastPermalink(lastPermalink)
            .build();

    return Stream.of(channelSub).toList();
  }
}
