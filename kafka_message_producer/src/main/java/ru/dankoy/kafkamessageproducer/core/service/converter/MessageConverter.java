package ru.dankoy.kafkamessageproducer.core.service.converter;

import java.util.List;
import ru.dankoy.kafkamessageproducer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.regisrty.SentCoubsRegistry;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.communitysubscription.CommunitySubscription;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.tagsubscription.TagSubscription;

public interface MessageConverter {

  List<CommunitySubscriptionMessage> convert(CommunitySubscription communitySubscription);

  CommunitySubscription convert(CommunitySubscriptionMessage communitySubscriptionMessage);

  List<TagSubscriptionMessage> convert(TagSubscription tagSubscription);

  TagSubscription convert(TagSubscriptionMessage tagSubscriptionMessage);

  SentCoubsRegistry convertToRegistry(TagSubscriptionMessage tagSubscriptionMessage);

  SentCoubsRegistry convertToRegistry(
      CommunitySubscriptionMessage communitySubscriptionMessage);
}
