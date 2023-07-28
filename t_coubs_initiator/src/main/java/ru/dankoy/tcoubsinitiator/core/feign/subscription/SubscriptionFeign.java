package ru.dankoy.tcoubsinitiator.core.feign.subscription;


import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.communitysubscription.CommunitySubscription;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.tagsubscription.TagSubscription;

@FeignClient(name = "subscriptions-holder")
public interface SubscriptionFeign {


  @GetMapping(path = "/api/v1/subscriptions/{communityName}")
  List<CommunitySubscription> getSubscriptionsByCommunityName(
      @PathVariable(value = "communityName") String communityName);

  @GetMapping(path = "/api/v1/subscriptions")
  List<CommunitySubscription> getAllSubscriptions();

  @GetMapping(path = "/api/v1/subscriptions", params = {"active"})
  List<CommunitySubscription> getAllSubscriptionsWithActiveChats(@RequestParam boolean active);

  @GetMapping(path = "/api/v1/tag_subscriptions/{tag}")
  List<TagSubscription> getSubscriptionsByTagTitle(
      @PathVariable(value = "tag") String tag);

  @GetMapping(path = "/api/v1/tag_subscriptions", params = {"active"})
  List<TagSubscription> getAllTagSubscriptionsWithActiveChats(
      @RequestParam boolean active);

}
