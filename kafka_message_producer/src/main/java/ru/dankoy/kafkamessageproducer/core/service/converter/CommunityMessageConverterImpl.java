package ru.dankoy.kafkamessageproducer.core.service.converter;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import ru.dankoy.kafkamessageproducer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.CommunitySubscription;


@Component
public class CommunityMessageConverterImpl implements CommunityMessageConverter {

  @Override
  public List<CommunitySubscriptionMessage> convert(CommunitySubscription communitySubscription) {

    return communitySubscription.getCoubs().stream()
        .map(c -> new CommunitySubscriptionMessage(
            communitySubscription.getId(),
            communitySubscription.getCommunity(),
            communitySubscription.getChat(),
            communitySubscription.getSection(),
            c,
            communitySubscription.getLastPermalink()
        ))
        .toList();
  }


  @Override
  public CommunitySubscription convert(CommunitySubscriptionMessage communitySubscriptionMessage) {

    return new CommunitySubscription(
        communitySubscriptionMessage.id(),
        communitySubscriptionMessage.community(),
        communitySubscriptionMessage.chat(),
        communitySubscriptionMessage.section(),
        communitySubscriptionMessage.coub().getPermalink(),
        new ArrayList<>()
    );

  }

}
