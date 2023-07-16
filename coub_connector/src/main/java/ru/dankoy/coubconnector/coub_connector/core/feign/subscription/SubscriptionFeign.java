package ru.dankoy.coubconnector.coub_connector.core.feign.subscription;


import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.dankoy.coubconnector.coub_connector.core.domain.subscriberholder.subscription.Subscription;

@FeignClient(name = "subscription-holder")
public interface SubscriptionFeign {


  @GetMapping(path = "/api/v1/subscriptions/{communityName}")
  List<Subscription> getSubscriptionsByCommunityName(
      @PathVariable(value = "communityName") String communityName);

  @GetMapping(path = "/api/v1/subscriptions")
  List<Subscription> getAllSubscriptions();

}
