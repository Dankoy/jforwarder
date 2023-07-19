package ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.subscription.Subscription;
import ru.dankoy.tcoubsinitiator.core.feign.messageproducer.MessageProducerFeign;

@Service
@RequiredArgsConstructor
public class MessageProducerServiceImpl implements MessageProducerService {

  private final MessageProducerFeign messageProducerFeign;


  public void sendSubscriptionsData(List<Subscription> subscriptions) {

    messageProducerFeign.sendSubscriptions(subscriptions);

  }

}
