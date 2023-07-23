package ru.dankoy.kafkamessageproducer.core.service.subscription;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.kafkamessageproducer.core.domain.message.SubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Subscription;
import ru.dankoy.kafkamessageproducer.core.feign.subscriptionsholder.subscription.SubscriptionFeign;
import ru.dankoy.kafkamessageproducer.core.service.converter.MessageConverter;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

  private final SubscriptionFeign subscriptionFeign;

  private final MessageConverter messageConverter;


  public Subscription updatePermalink(SubscriptionMessage subscriptionMessage) {

    var subscription = messageConverter.convert(subscriptionMessage);

    return subscriptionFeign.updatePermalink(subscription);

  }

}
