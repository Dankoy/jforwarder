package ru.dankoy.kafkamessageconsumer.core.service.converter;

import org.springframework.stereotype.Component;
import ru.dankoy.kafkamessageconsumer.core.domain.coub.Coub;
import ru.dankoy.kafkamessageconsumer.core.domain.message.ChannelSubscriptionMessage;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageconsumer.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Chat;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Order;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Scope;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Type;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.channelsubscription.Channel;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.communitysubscription.Community;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.communitysubscription.Section;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.tagsubscription.Tag;

@Component
public class MessageConverterProtobufImpl implements MessageConverterProtobuf {

  @Override
  public CommunitySubscriptionMessage convert(
      ru.dankoy.protobufschemas.protos.domain.subscription.community.CommunitySubscription
          communitySubscription) {

    var chat =
        new Chat(
            communitySubscription.getChat().getId(),
            communitySubscription.getChat().getChatId(),
            communitySubscription.getChat().hasMessageThreadId()
                ? communitySubscription.getChat().getMessageThreadId().getValue()
                : null,
            communitySubscription.getChat().getUsername());

    var section =
        new Section(
            communitySubscription.getSection().getId(),
            communitySubscription.getSection().getName());

    var community =
        new Community(
            communitySubscription.getCommunity().getId(),
            communitySubscription.getCommunity().getExternalId(),
            communitySubscription.getCommunity().getName());

    var coub =
        new Coub(
            communitySubscription.getCoub().getId(),
            communitySubscription.getCoub().getTitle(),
            communitySubscription.getCoub().getPermalink(),
            communitySubscription.getCoub().getUrl(),
            null);

    return CommunitySubscriptionMessage.builder()
        .id(communitySubscription.getId())
        .lastPermalink(communitySubscription.getLastPermalink())
        .chat(chat)
        .section(section)
        .community(community)
        .coub(coub)
        .build();
  }

  @Override
  public TagSubscriptionMessage convert(
      ru.dankoy.protobufschemas.protos.domain.subscription.tag.TagSubscription tagSubscription) {

    var chat =
        new Chat(
            tagSubscription.getChat().getId(),
            tagSubscription.getChat().getChatId(),
            tagSubscription.getChat().hasMessageThreadId()
                ? tagSubscription.getChat().getMessageThreadId().getValue()
                : null,
            tagSubscription.getChat().getUsername());

    var order =
        new Order(
            tagSubscription.getOrder().getId(),
            tagSubscription.getOrder().getName(),
            tagSubscription.getOrder().getValue());

    var scope = new Scope(tagSubscription.getScope().getId(), tagSubscription.getScope().getName());

    var type = new Type(tagSubscription.getType().getId(), tagSubscription.getType().getName());

    var tag = new Tag(tagSubscription.getTag().getId(), tagSubscription.getTag().getTitle());

    var coub =
        new Coub(
            tagSubscription.getCoub().getId(),
            tagSubscription.getCoub().getTitle(),
            tagSubscription.getCoub().getPermalink(),
            tagSubscription.getCoub().getUrl(),
            null);

    return TagSubscriptionMessage.builder()
        .id(tagSubscription.getId())
        .lastPermalink(tagSubscription.getLastPermalink())
        .chat(chat)
        .order(order)
        .scope(scope)
        .type(type)
        .tag(tag)
        .coub(coub)
        .build();
  }

  @Override
  public ChannelSubscriptionMessage convert(
      ru.dankoy.protobufschemas.protos.domain.subscription.channel.ChannelSubscription
          channelSubscription) {
    var chat =
        new Chat(
            channelSubscription.getChat().getId(),
            channelSubscription.getChat().getChatId(),
            channelSubscription.getChat().hasMessageThreadId()
                ? channelSubscription.getChat().getMessageThreadId().getValue()
                : null,
            channelSubscription.getChat().getUsername());

    var order =
        new Order(
            channelSubscription.getOrder().getId(),
            channelSubscription.getOrder().getName(),
            channelSubscription.getOrder().getValue());

    var scope =
        new Scope(channelSubscription.getScope().getId(), channelSubscription.getScope().getName());

    var type =
        new Type(channelSubscription.getType().getId(), channelSubscription.getType().getName());

    var channel =
        new Channel(
            channelSubscription.getChannel().getId(),
            channelSubscription.getChannel().getTitle(),
            channelSubscription.getChannel().getPermalink());

    var coub =
        new Coub(
            channelSubscription.getCoub().getId(),
            channelSubscription.getCoub().getTitle(),
            channelSubscription.getCoub().getPermalink(),
            channelSubscription.getCoub().getUrl(),
            null);

    return ChannelSubscriptionMessage.builder()
        .id(channelSubscription.getId())
        .lastPermalink(channelSubscription.getLastPermalink())
        .chat(chat)
        .order(order)
        .scope(scope)
        .type(type)
        .channel(channel)
        .coub(coub)
        .build();
  }
}
