package ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.channelsubscription.ChannelSubscription;
import ru.dankoy.tcoubsinitiator.core.httpservice.messageproducer.MessageProducerHttpService;

@Primary
@RequiredArgsConstructor
@Service
public class MessageProducerChannelSubscriptionServiceHttpClient
    implements MessageProducerChannelSubscriptionService {

  private final MessageProducerHttpService messageProducerHttpService;

  @Override
  public void sendChannelSubscriptionsData(List<ChannelSubscription> channelSubscriptions) {

    messageProducerHttpService.sendChannelSubscriptionsProtobuf(channelSubscriptions);
  }
}
