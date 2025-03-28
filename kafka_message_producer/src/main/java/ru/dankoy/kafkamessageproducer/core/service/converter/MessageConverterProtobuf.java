package ru.dankoy.kafkamessageproducer.core.service.converter;

import java.util.List;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.channelsubscription.ChannelSubscription;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.communitysubscription.CommunitySubscription;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.tagsubscription.TagSubscription;

public interface MessageConverterProtobuf {

  List<ru.dankoy.protobufschemas.protos.domain.subscription.community.CommunitySubscription>
      convert(CommunitySubscription communitySubscription);

  List<ru.dankoy.protobufschemas.protos.domain.subscription.tag.TagSubscription> convert(
      TagSubscription tagSubscription);

  List<ru.dankoy.protobufschemas.protos.domain.subscription.channel.ChannelSubscription> convert(
      ChannelSubscription channelSubscription);
}
