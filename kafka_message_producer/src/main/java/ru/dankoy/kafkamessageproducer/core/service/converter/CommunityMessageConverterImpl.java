package ru.dankoy.kafkamessageproducer.core.service.converter;

import java.util.List;
import org.springframework.stereotype.Component;
import ru.dankoy.kafkamessageproducer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.communitysubscription.CommunitySubscription;


/**
 * @deprecated use {@link MessageConverter}
 */
@Deprecated(since = "2024-01-30")
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

    return CommunitySubscription.builder()
        .id(communitySubscriptionMessage.id())
        .community(communitySubscriptionMessage.community())
        .chat(communitySubscriptionMessage.chat())
        .section(communitySubscriptionMessage.section())
        .lastPermalink(communitySubscriptionMessage.coub().getPermalink())
        .build();

  }

}
