package ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.tagsubscription.TagSubscription;
import ru.dankoy.tcoubsinitiator.core.feign.messageproducer.MessageProducerFeign;

/**
 * @deprecated since spring-boot 4.0.0 in favor {@link ChannelSubscriptionServiceHttpClient}
 */
@Deprecated(since = "2025-01-04", forRemoval = true)
@RequiredArgsConstructor
@Service
public class MessageProducerTagSubscriptionServiceImpl
    implements MessageProducerTagSubscriptionService {

  private final MessageProducerFeign messageProducerFeign;

  @Override
  public void sendTagSubscriptionsData(List<TagSubscription> tagSubscriptions) {

    messageProducerFeign.sendTagSubscriptionsProtobuf(tagSubscriptions);
  }
}
