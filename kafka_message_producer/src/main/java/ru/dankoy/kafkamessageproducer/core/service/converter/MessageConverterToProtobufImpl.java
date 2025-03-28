package ru.dankoy.kafkamessageproducer.core.service.converter;

import com.google.protobuf.Int32Value;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.channelsubscription.ChannelSubscription;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.communitysubscription.CommunitySubscription;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.tagsubscription.TagSubscription;
import ru.dankoy.protobufschemas.protos.domain.chat.Chat;
import ru.dankoy.protobufschemas.protos.domain.coub.Coub;
import ru.dankoy.protobufschemas.protos.domain.subscription.Order;
import ru.dankoy.protobufschemas.protos.domain.subscription.Scope;
import ru.dankoy.protobufschemas.protos.domain.subscription.Type;
import ru.dankoy.protobufschemas.protos.domain.subscription.channel.Channel;
import ru.dankoy.protobufschemas.protos.domain.subscription.community.Community;
import ru.dankoy.protobufschemas.protos.domain.subscription.community.Section;
import ru.dankoy.protobufschemas.protos.domain.subscription.tag.Tag;

@Component
public class MessageConverterToProtobufImpl implements MessageConverterProtobuf {

  @Override
  public List<ru.dankoy.protobufschemas.protos.domain.subscription.community.CommunitySubscription>
      convert(CommunitySubscription communitySubscription) {

    var builder =
        Chat.newBuilder()
            .setId(communitySubscription.getChat().getId())
            .setChatId(communitySubscription.getChat().getChatId());

    Optional.ofNullable(communitySubscription.getChat().getMessageThreadId())
        .ifPresent(i -> builder.setMessageThreadId(Int32Value.of(i)));
    Optional.ofNullable(communitySubscription.getChat().getUsername())
        .ifPresent(builder::setUsername);

    var chat = builder.build();

    var community =
        Community.newBuilder()
            .setId(communitySubscription.getCommunity().getId())
            .setName(communitySubscription.getCommunity().getName())
            .setExternalId(communitySubscription.getCommunity().getExternalId())
            .build();

    var section =
        Section.newBuilder()
            .setId(communitySubscription.getSection().getId())
            .setName(communitySubscription.getSection().getName())
            .build();

    return communitySubscription.getCoubs().stream()
        .map(
            c ->
                ru.dankoy.protobufschemas.protos.domain.subscription.community.CommunitySubscription
                    .newBuilder()
                    .setId(communitySubscription.getId())
                    .setChat(chat)
                    .setCommunity(community)
                    .setSection(section)
                    .setCoub(
                        Coub.newBuilder()
                            .setId(c.getId())
                            .setTitle(c.getTitle())
                            .setPermalink(c.getPermalink())
                            .build())
                    .setLastPermalink(communitySubscription.getLastPermalink())
                    .build())
        .toList();
  }

  @Override
  public List<ru.dankoy.protobufschemas.protos.domain.subscription.tag.TagSubscription> convert(
      TagSubscription tagSubscription) {
    var builder =
        Chat.newBuilder()
            .setId(tagSubscription.getChat().getId())
            .setChatId(tagSubscription.getChat().getChatId());

    Optional.ofNullable(tagSubscription.getChat().getMessageThreadId())
        .ifPresent(i -> builder.setMessageThreadId(Int32Value.of(i)));
    Optional.ofNullable(tagSubscription.getChat().getUsername()).ifPresent(builder::setUsername);

    var chat = builder.build();

    var tag =
        Tag.newBuilder()
            .setId(tagSubscription.getTag().getId())
            .setTitle(tagSubscription.getTag().getTitle())
            .build();

    var scope =
        Scope.newBuilder()
            .setId(tagSubscription.getScope().getId())
            .setName(tagSubscription.getScope().getName())
            .build();

    var orderBuilder = Order.newBuilder().setId(tagSubscription.getOrder().getId());

    Optional.ofNullable(tagSubscription.getOrder().getName()).ifPresent(orderBuilder::setName);
    Optional.ofNullable(tagSubscription.getOrder().getValue()).ifPresent(orderBuilder::setValue);

    var order = orderBuilder.build();

    var type =
        Type.newBuilder()
            .setId(tagSubscription.getType().getId())
            .setName(tagSubscription.getType().getName())
            .build();

    return tagSubscription.getCoubs().stream()
        .map(
            c ->
                ru.dankoy.protobufschemas.protos.domain.subscription.tag.TagSubscription
                    .newBuilder()
                    .setId(tagSubscription.getId())
                    .setChat(chat)
                    .setOrder(order)
                    .setType(type)
                    .setScope(scope)
                    .setTag(tag)
                    .setCoub(
                        Coub.newBuilder()
                            .setId(c.getId())
                            .setTitle(c.getTitle())
                            .setPermalink(c.getPermalink())
                            .build())
                    .setLastPermalink(tagSubscription.getLastPermalink())
                    .build())
        .toList();
  }

  @Override
  public List<ru.dankoy.protobufschemas.protos.domain.subscription.channel.ChannelSubscription>
      convert(ChannelSubscription channelSubscription) {

    var builder =
        Chat.newBuilder()
            .setId(channelSubscription.getChat().getId())
            .setChatId(channelSubscription.getChat().getChatId());

    Optional.ofNullable(channelSubscription.getChat().getMessageThreadId())
        .ifPresent(i -> builder.setMessageThreadId(Int32Value.of(i)));
    Optional.ofNullable(channelSubscription.getChat().getUsername())
        .ifPresent(builder::setUsername);

    var chat = builder.build();

    var channel =
        Channel.newBuilder()
            .setId(channelSubscription.getChannel().getId())
            .setTitle(channelSubscription.getChannel().getTitle())
            .setPermalink(channelSubscription.getChannel().getPermalink())
            .build();

    var scope =
        Scope.newBuilder()
            .setId(channelSubscription.getScope().getId())
            .setName(channelSubscription.getScope().getName())
            .build();

    var orderBuilder = Order.newBuilder().setId(channelSubscription.getOrder().getId());

    Optional.ofNullable(channelSubscription.getOrder().getName()).ifPresent(orderBuilder::setName);
    Optional.ofNullable(channelSubscription.getOrder().getValue())
        .ifPresent(orderBuilder::setValue);

    var order = orderBuilder.build();

    var type =
        Type.newBuilder()
            .setId(channelSubscription.getType().getId())
            .setName(channelSubscription.getType().getName())
            .build();

    return channelSubscription.getCoubs().stream()
        .map(
            c ->
                ru.dankoy.protobufschemas.protos.domain.subscription.channel.ChannelSubscription
                    .newBuilder()
                    .setId(channelSubscription.getId())
                    .setChat(chat)
                    .setOrder(order)
                    .setType(type)
                    .setScope(scope)
                    .setChannel(channel)
                    .setCoub(
                        Coub.newBuilder()
                            .setId(c.getId())
                            .setTitle(c.getTitle())
                            .setPermalink(c.getPermalink())
                            .build())
                    .setLastPermalink(channelSubscription.getLastPermalink())
                    .build())
        .toList();
  }
}
