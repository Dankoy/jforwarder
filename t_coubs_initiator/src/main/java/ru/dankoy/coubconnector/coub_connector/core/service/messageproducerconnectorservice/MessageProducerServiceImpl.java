package ru.dankoy.coubconnector.coub_connector.core.service.messageproducerconnectorservice;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.coubconnector.coub_connector.core.domain.subscribtionsholder.subscription.Subscription;
import ru.dankoy.coubconnector.coub_connector.core.feign.messageproducer.MessageProducerFeign;

@Service
@RequiredArgsConstructor
public class MessageProducerServiceImpl implements MessageProducerService {

  private final MessageProducerFeign messageProducerFeign;


  public void sendSubscriptionsData(List<Subscription> subscriptions) {

    messageProducerFeign.sendSubscriptions(subscriptions);

  }

}
