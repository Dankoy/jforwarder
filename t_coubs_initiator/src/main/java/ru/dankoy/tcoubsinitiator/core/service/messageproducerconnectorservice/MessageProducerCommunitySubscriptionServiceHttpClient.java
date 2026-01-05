package ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.communitysubscription.CommunitySubscription;
import ru.dankoy.tcoubsinitiator.core.httpservice.messageproducer.MessageProducerHttpService;

@Primary
@Service
@RequiredArgsConstructor
public class MessageProducerCommunitySubscriptionServiceHttpClient
    implements MessageProducerCommunitySubscriptionService {

  private final MessageProducerHttpService messageProducerHttpService;

  public void sendCommunitySubscriptionsData(List<CommunitySubscription> communitySubscriptions) {

    messageProducerHttpService.sendCommunitySubscriptionsProtobuf(communitySubscriptions);
  }
}
