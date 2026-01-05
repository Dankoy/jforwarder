package ru.dankoy.kafkamessageconsumer.core.service.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CoubMessage;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Subscription;
import ru.dankoy.kafkamessageconsumer.core.httpservice.subscriptionsholder.SubscriptionsHolderHttpService;
import ru.dankoy.kafkamessageconsumer.core.service.converter.MessageConverter;

@Service("subscriptionServiceHttpClient")
@RequiredArgsConstructor
public class SubscriptionServiceHttpClient implements SubscriptionService {

  private final SubscriptionsHolderHttpService subscriptionHttpService;

  private final MessageConverter messageConverter;

  @Override
  public Subscription updatePermalink(CoubMessage coubMessage) {

    var subscription = messageConverter.convert(coubMessage);
    return subscriptionHttpService.updateSubscriptionPermalink(subscription);
  }
}
