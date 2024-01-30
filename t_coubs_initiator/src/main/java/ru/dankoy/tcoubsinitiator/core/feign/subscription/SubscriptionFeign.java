package ru.dankoy.tcoubsinitiator.core.feign.subscription;


import java.time.LocalDateTime;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.communitysubscription.CommunitySubscription;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.registry.SentCoubsRegistry;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.tagsubscription.TagSubscription;

@FeignClient(name = "subscriptions-holder")
public interface SubscriptionFeign {


  @GetMapping(path = "/api/v1/community_subscriptions/{communityName}")
  List<CommunitySubscription> getSubscriptionsByCommunityName(
      @PathVariable(value = "communityName") String communityName);

  @GetMapping(path = "/api/v1/community_subscriptions", params = {"page", "size", "sort"})
  Page<CommunitySubscription> getAllSubscriptions(Pageable pageable);

  @GetMapping(path = "/api/v1/community_subscriptions", params = {"active", "page", "size", "sort"})
  Page<CommunitySubscription> getAllSubscriptionsWithActiveChats(@RequestParam boolean active,
      Pageable pageable);

  @GetMapping(path = "/api/v1/tag_subscriptions/{tag}")
  List<TagSubscription> getSubscriptionsByTagTitle(
      @PathVariable(value = "tag") String tag);

  @GetMapping(path = "/api/v1/tag_subscriptions", params = {"active", "page", "size", "sort"})
  Page<TagSubscription> getAllTagSubscriptionsWithActiveChats(
      @RequestParam boolean active, Pageable pageable);

  @GetMapping(path = "/api/v1/sent_coubs_registry", params = {"page", "size", "sort"})
  Page<SentCoubsRegistry> getAll();

  @GetMapping(path = "/api/v1/sent_coubs_registry",
      params = {"subscriptionId", "page", "size", "sort"})
  Page<SentCoubsRegistry> getAllBySubscriptionId(@RequestParam long subscriptionId,
      Pageable pageable);

  @GetMapping(path = "/api/v1/sent_coubs_registry",
      params = {"subscriptionId", "dateTime", "page", "size", "sort"})
  Page<SentCoubsRegistry> getAllBySubscriptionIdAndDateAfter(@RequestParam long subscriptionId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
      Pageable pageable);

  @GetMapping(path = "/api/v1/sent_coubs_registry")
  SentCoubsRegistry create(@RequestBody SentCoubsRegistry sentCoubsRegistry);

  @GetMapping(path = "/api/v1/sent_coubs_registry/{id}")
  void delete(@PathVariable long id);

}
