package ru.dankoy.kafkamessageproducer.core.feign.subscriptionsholder.subscription;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Subscription;

@FeignClient(name = "subscriptions-holder")
public interface SubscriptionFeign {


  @PutMapping(path = "/api/v1/subscriptions")
  Subscription updatePermalink(@RequestBody Subscription subscription);

}
