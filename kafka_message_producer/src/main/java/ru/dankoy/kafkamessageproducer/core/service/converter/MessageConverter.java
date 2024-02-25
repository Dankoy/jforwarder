package ru.dankoy.kafkamessageproducer.core.service.converter;

import java.util.List;
import ru.dankoy.kafkamessageproducer.core.domain.message.ChannelSubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.message.CoubMessage;
import ru.dankoy.kafkamessageproducer.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.registry.SentCoubsRegistry;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Subscription;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.channelsubscription.ChannelSubscription;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.communitysubscription.CommunitySubscription;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.tagsubscription.TagSubscription;

public interface MessageConverter {

  List<CommunitySubscriptionMessage> convert(CommunitySubscription communitySubscription);

  List<TagSubscriptionMessage> convert(TagSubscription tagSubscription);

  List<ChannelSubscriptionMessage> convert(ChannelSubscription channelSubscription);

  SentCoubsRegistry convertToRegistry(CoubMessage message);

  Subscription convert(CoubMessage message);
}
