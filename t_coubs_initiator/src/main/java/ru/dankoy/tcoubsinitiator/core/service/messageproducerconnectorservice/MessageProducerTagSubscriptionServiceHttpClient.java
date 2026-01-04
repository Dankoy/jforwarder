package ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.tagsubscription.TagSubscription;
import ru.dankoy.tcoubsinitiator.core.httpservice.messageproducer.MessageProducerHttpService;

@Primary
@RequiredArgsConstructor
@Service
public class MessageProducerTagSubscriptionServiceHttpClient
    implements MessageProducerTagSubscriptionService {

  private final MessageProducerHttpService messageProducerHttpService;

  @Override
  public void sendTagSubscriptionsData(List<TagSubscription> tagSubscriptions) {

    messageProducerHttpService.sendTagSubscriptionsProtobuf(tagSubscriptions);
  }
}
