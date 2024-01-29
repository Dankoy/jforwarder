package ru.dankoy.kafkamessageproducer.core.feign.subscriptionsholder.subscription;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.CommunitySubscription;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.tagsubscription.TagSubscription;

@FeignClient(name = "subscriptions-holder")
public interface SubscriptionFeign {

  @PutMapping(path = "/api/v1/community_subscriptions")
  CommunitySubscription updateCommunitySubscriptionPermalink(
      @RequestBody CommunitySubscription communitySubscription);


  @PutMapping(path = "/api/v1/tag_subscriptions")
  TagSubscription updateTagSubscriptionPermalink(
      @RequestBody TagSubscription tagSubscription);


}
