package ru.dankoy.kafkamessageproducer.core.service.converter;

import java.util.List;
import ru.dankoy.kafkamessageproducer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.communitysubscription.CommunitySubscription;

/**
 * @deprecated use {@link MessageConverter}
 */
@Deprecated(since = "2024-01-30")
public interface CommunityMessageConverter {

  List<CommunitySubscriptionMessage> convert(CommunitySubscription communitySubscription);

  CommunitySubscription convert(CommunitySubscriptionMessage communitySubscriptionMessage);
}
