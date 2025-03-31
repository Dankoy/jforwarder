package ru.dankoy.kafkamessageconsumer.core.service.converter;

import ru.dankoy.kafkamessageconsumer.core.domain.message.ChannelSubscriptionMessage;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageconsumer.core.domain.message.TagSubscriptionMessage;

public interface MessageConverterProtobuf {

  CommunitySubscriptionMessage convert(
      ru.dankoy.protobufschemas.protos.domain.subscription.community.CommunitySubscription
          communitySubscription);

  TagSubscriptionMessage convert(
      ru.dankoy.protobufschemas.protos.domain.subscription.tag.TagSubscription tagSubscription);

  ChannelSubscriptionMessage convert(
      ru.dankoy.protobufschemas.protos.domain.subscription.channel.ChannelSubscription
          channelSubscription);
}
