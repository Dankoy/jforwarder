package ru.dankoy.tcoubsinitiator.core.feign.subscription;

import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.channelsubscription.ChannelSubscription;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.communitysubscription.CommunitySubscription;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.tagsubscription.TagSubscription;

/**
 * @deprecated since spring-boot 4.0.0 in favor {@link ChannelSubscriptionServiceHttpClient}
 */
@Deprecated(since = "2025-01-04", forRemoval = true)
@FeignClient(contextId = "subscriptions", name = "subscriptions-holder")
public interface SubscriptionFeign {

  @GetMapping(path = "/api/v1/community_subscriptions/{communityName}")
  List<CommunitySubscription> getSubscriptionsByCommunityName(
      @PathVariable(value = "communityName") String communityName);

  @GetMapping(
      path = "/api/v1/community_subscriptions",
      params = {"page", "size", "sort"})
  Page<CommunitySubscription> getAllSubscriptions(Pageable pageable);

  @GetMapping(
      path = "/api/v1/community_subscriptions",
      params = {"active", "page", "size", "sort"})
  Page<CommunitySubscription> getAllSubscriptionsWithActiveChats(
      @RequestParam boolean active, Pageable pageable);

  @GetMapping(path = "/api/v1/tag_subscriptions/{tag}")
  List<TagSubscription> getSubscriptionsByTagTitle(@PathVariable(value = "tag") String tag);

  @GetMapping(
      path = "/api/v1/tag_subscriptions",
      params = {"active", "page", "size", "sort"})
  Page<TagSubscription> getAllTagSubscriptionsWithActiveChats(
      @RequestParam boolean active, Pageable pageable);

  @GetMapping(
      path = "/api/v1/channel_subscriptions",
      params = {"active", "page", "size", "sort"})
  Page<ChannelSubscription> getAllChannelSubscriptionsWithActiveChats(
      @RequestParam boolean active, Pageable pageable);

  @GetMapping(
      path = "/api/v1/channel_subscriptions",
      params = {"chatUuids", "page", "size", "sort"})
  Page<ChannelSubscription> getChannelSubscriptionByChatUuids(
      @RequestParam(value = "chatUuids") List<UUID> chatUuids, Pageable pageable);

  @GetMapping(
      path = "/api/v1/community_subscriptions",
      params = {"chatUuids", "page", "size", "sort"})
  Page<CommunitySubscription> getCommunitySubscriptionByChatUuids(
      @RequestParam(value = "chatUuids") List<UUID> chatUuids, Pageable pageable);

  @GetMapping(
      path = "/api/v1/tag_subscriptions",
      params = {"chatUuids", "page", "size", "sort"})
  Page<TagSubscription> getTagSubscriptionByChatUuids(
      @RequestParam(value = "chatUuids") List<UUID> chatUuids, Pageable pageable);
}
