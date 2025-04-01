package ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.communitysubscription.CommunitySubscription;
import ru.dankoy.tcoubsinitiator.core.feign.messageproducer.MessageProducerFeign;

@Service
@RequiredArgsConstructor
public class MessageProducerCommunitySubscriptionServiceImpl
    implements MessageProducerCommunitySubscriptionService {

  private final MessageProducerFeign messageProducerFeign;

  public void sendCommunitySubscriptionsData(List<CommunitySubscription> communitySubscriptions) {

    messageProducerFeign.sendCommunitySubscriptionsProtobuf(communitySubscriptions);
  }
}
