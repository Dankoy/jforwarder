package ru.dankoy.tcoubsinitiator.core.feign.messageproducer;


import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.subscription.Subscription;

@FeignClient(name = "message-producer")
public interface MessageProducerFeign {

  @GetMapping(path = "/api/v1/produce")
  void sendSubscriptions(@RequestBody List<Subscription> subscriptions);

}
