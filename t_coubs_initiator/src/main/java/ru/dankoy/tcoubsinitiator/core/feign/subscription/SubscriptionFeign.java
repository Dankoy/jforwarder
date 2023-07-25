package ru.dankoy.tcoubsinitiator.core.feign.subscription;


import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.subscription.Subscription;

@FeignClient(name = "subscriptions-holder")
public interface SubscriptionFeign {


  @GetMapping(path = "/api/v1/subscriptions/{communityName}")
  List<Subscription> getSubscriptionsByCommunityName(
      @PathVariable(value = "communityName") String communityName);

  @GetMapping(path = "/api/v1/subscriptions")
  List<Subscription> getAllSubscriptions();

  @GetMapping(path = "/api/v1/subscriptions", params = {"active"})
  List<Subscription> getAllSubscriptionsWithActiveChats(boolean active);

}
