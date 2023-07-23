package ru.dankoy.kafkamessageproducer.core.service.converter;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import ru.dankoy.kafkamessageproducer.core.domain.message.SubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Subscription;


@Component
public class MessageConverterImpl implements MessageConverter {

  @Override
  public List<SubscriptionMessage> convert(Subscription subscription) {

    return subscription.getCoubs().stream()
        .map(c -> new SubscriptionMessage(
            subscription.getId(),
            subscription.getCommunity(),
            subscription.getChat(),
            subscription.getSection(),
            c,
            subscription.getLastPermalink()
        ))
        .toList();
  }


  @Override
  public Subscription convert(SubscriptionMessage subscriptionMessage) {

    return new Subscription(
        subscriptionMessage.id(),
        subscriptionMessage.community(),
        subscriptionMessage.chat(),
        subscriptionMessage.section(),
        subscriptionMessage.coub().getPermalink(),
        new ArrayList<>()
    );

  }

}
