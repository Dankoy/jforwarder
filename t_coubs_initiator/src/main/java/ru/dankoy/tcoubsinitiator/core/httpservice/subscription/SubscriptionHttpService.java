package ru.dankoy.tcoubsinitiator.core.httpservice.subscription;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import ru.dankoy.tcoubsinitiator.core.domain.page.RestPageImpl;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.channelsubscription.ChannelSubscription;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.communitysubscription.CommunitySubscription;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.tagsubscription.TagSubscription;

@HttpExchange(url = "http://subscriptions-holder")
public interface SubscriptionHttpService {

  @GetExchange(url = "/api/v1/community_subscriptions/{communityName}")
  List<CommunitySubscription> getSubscriptionsByCommunityName(
      @PathVariable(value = "communityName") String communityName);

  @GetExchange(url = "/api/v1/community_subscriptions")
  RestPageImpl<CommunitySubscription> getAllSubscriptions(Pageable pageable);

  @GetExchange(url = "/api/v1/community_subscriptions")
  RestPageImpl<CommunitySubscription> getAllSubscriptionsWithActiveChats(
      @RequestParam boolean active, Pageable pageable);

  @GetExchange(url = "/api/v1/tag_subscriptions/{tag}")
  List<TagSubscription> getSubscriptionsByTagTitle(@PathVariable(value = "tag") String tag);

  @GetExchange(url = "/api/v1/tag_subscriptions")
  RestPageImpl<TagSubscription> getAllTagSubscriptionsWithActiveChats(
      @RequestParam boolean active, Pageable pageable);

  @GetExchange(url = "/api/v1/channel_subscriptions")
  RestPageImpl<ChannelSubscription> getAllChannelSubscriptionsWithActiveChats(
      @RequestParam boolean active, Pageable pageable);

  @GetExchange(url = "/api/v1/channel_subscriptions")
  RestPageImpl<ChannelSubscription> getChannelSubscriptionByChatUuids(
      @RequestParam(value = "chatUuids") List<UUID> chatUuids, Pageable pageable);

  @GetExchange(url = "/api/v1/community_subscriptions")
  RestPageImpl<CommunitySubscription> getCommunitySubscriptionByChatUuids(
      @RequestParam(value = "chatUuids") List<UUID> chatUuids, Pageable pageable);

  @GetExchange(url = "/api/v1/tag_subscriptions")
  RestPageImpl<TagSubscription> getTagSubscriptionByChatUuids(
      @RequestParam(value = "chatUuids") List<UUID> chatUuids, Pageable pageable);
}
