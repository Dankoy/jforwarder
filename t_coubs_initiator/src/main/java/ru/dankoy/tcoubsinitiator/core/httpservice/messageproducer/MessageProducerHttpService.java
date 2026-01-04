package ru.dankoy.tcoubsinitiator.core.httpservice.messageproducer;

import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.channelsubscription.ChannelSubscription;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.communitysubscription.CommunitySubscription;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.tagsubscription.TagSubscription;

@HttpExchange(url = "http://kafka-message-producer")
public interface MessageProducerHttpService {

  @PostExchange(url = "/api/v1/subscriptions")
  void sendCommunitySubscriptions(@RequestBody List<CommunitySubscription> communitySubscriptions);

  @PostExchange(url = "/api/v1/tag_subscriptions")
  void sendTagSubscriptions(@RequestBody List<TagSubscription> tagSubscriptions);

  @PostExchange(url = "/api/v1/channel_subscriptions")
  void sendChannelSubscriptions(@RequestBody List<ChannelSubscription> channelSubscriptions);

  @PostExchange(url = "/api/v1/subscriptions_protobuf")
  void sendCommunitySubscriptionsProtobuf(
      @RequestBody List<CommunitySubscription> communitySubscriptions);

  @PostExchange(url = "/api/v1/tag_subscriptions_protobuf")
  void sendTagSubscriptionsProtobuf(@RequestBody List<TagSubscription> tagSubscriptions);

  @PostExchange(url = "/api/v1/channel_subscriptions_protobuf")
  void sendChannelSubscriptionsProtobuf(
      @RequestBody List<ChannelSubscription> channelSubscriptions);
}
