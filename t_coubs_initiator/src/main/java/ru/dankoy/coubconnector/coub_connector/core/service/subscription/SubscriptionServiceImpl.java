package ru.dankoy.coubconnector.coub_connector.core.service.subscription;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.coubconnector.coub_connector.core.domain.subscribtionsholder.subscription.Subscription;
import ru.dankoy.coubconnector.coub_connector.core.feign.subscription.SubscriptionFeign;


@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

  private final SubscriptionFeign subscriptionFeign;

  @Override
  public List<Subscription> getAllSubscriptions() {
    return subscriptionFeign.getAllSubscriptions();
  }
}
