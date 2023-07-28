package ru.dankoy.tcoubsinitiator.core.feign.messageproducer;


import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.communitysubscription.CommunitySubscription;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.tagsubscription.TagSubscription;

@FeignClient(name = "kafka-message-producer")
public interface MessageProducerFeign {

  @GetMapping(path = "/api/v1/subscriptions")
  void sendCommunitySubscriptions(@RequestBody List<CommunitySubscription> communitySubscriptions);

  @GetMapping(path = "/api/v1/tag_subscriptions")
  void sendTagSubscriptions(@RequestBody List<TagSubscription> tagSubscriptions);

}
