package ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.channelsubscription.ChannelSubscription;
import ru.dankoy.tcoubsinitiator.core.feign.messageproducer.MessageProducerFeign;

@RequiredArgsConstructor
@Service
public class MessageProducerChannelSubscriptionServiceImpl
    implements MessageProducerChannelSubscriptionService {

  private final MessageProducerFeign messageProducerFeign;

  @Override
  public void sendChannelSubscriptionsData(List<ChannelSubscription> channelSubscriptions) {

    messageProducerFeign.sendChannelSubscriptionsProtobuf(channelSubscriptions);
  }
}
