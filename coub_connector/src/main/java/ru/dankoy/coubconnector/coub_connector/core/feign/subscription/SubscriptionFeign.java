package ru.dankoy.coubconnector.coub_connector.core.feign.subscription;


import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.dankoy.coubconnector.coub_connector.core.domain.subscriberholder.subscriber.Subscription;

@FeignClient(name = "subscribers", url = "http://subscriptions_holder/api/v1")
public interface SubscriptionFeign {


  @GetMapping(path = "/{communityId}")
  List<Subscription> getSubscriptionsByCommunityId(
      @PathVariable(value = "communityId") String communityId);

}
