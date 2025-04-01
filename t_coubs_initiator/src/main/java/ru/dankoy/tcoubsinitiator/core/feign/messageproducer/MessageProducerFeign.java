package ru.dankoy.tcoubsinitiator.core.feign.messageproducer;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.channelsubscription.ChannelSubscription;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.communitysubscription.CommunitySubscription;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.tagsubscription.TagSubscription;

@FeignClient(name = "kafka-message-producer")
public interface MessageProducerFeign {

  @PostMapping(path = "/api/v1/subscriptions")
  void sendCommunitySubscriptions(@RequestBody List<CommunitySubscription> communitySubscriptions);

  @PostMapping(path = "/api/v1/tag_subscriptions")
  void sendTagSubscriptions(@RequestBody List<TagSubscription> tagSubscriptions);

  @PostMapping(path = "/api/v1/channel_subscriptions")
  void sendChannelSubscriptions(@RequestBody List<ChannelSubscription> channelSubscriptions);

  @PostMapping(path = "/api/v1/subscriptions_protobuf")
  void sendCommunitySubscriptionsProtobuf(
      @RequestBody List<CommunitySubscription> communitySubscriptions);

  @PostMapping(path = "/api/v1/tag_subscriptions_protobuf")
  void sendTagSubscriptionsProtobuf(@RequestBody List<TagSubscription> tagSubscriptions);

  @PostMapping(path = "/api/v1/channel_subscriptions_protobuf")
  void sendChannelSubscriptionsProtobuf(
      @RequestBody List<ChannelSubscription> channelSubscriptions);
}
