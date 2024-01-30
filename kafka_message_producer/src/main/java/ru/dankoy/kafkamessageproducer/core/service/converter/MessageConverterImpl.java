package ru.dankoy.kafkamessageproducer.core.service.converter;

import java.util.List;
import org.springframework.stereotype.Component;
import ru.dankoy.kafkamessageproducer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.regisrty.SentCoubsRegistry;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Subscription;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.communitysubscription.CommunitySubscription;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.tagsubscription.TagSubscription;

@Component
public class MessageConverterImpl implements MessageConverter {

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

  @Override
  public List<TagSubscriptionMessage> convert(TagSubscription tagSubscription) {
    return tagSubscription.getCoubs().stream()
        .map(c -> new TagSubscriptionMessage(
            tagSubscription.getId(),
            tagSubscription.getTag(),
            tagSubscription.getChat(),
            tagSubscription.getOrder(),
            tagSubscription.getScope(),
            tagSubscription.getType(),
            c,
            tagSubscription.getLastPermalink()
        ))
        .toList();
  }

  @Override
  public TagSubscription convert(TagSubscriptionMessage tagSubscriptionMessage) {
    return TagSubscription.builder()
        .id(tagSubscriptionMessage.id())
        .tag(tagSubscriptionMessage.tag())
        .chat(tagSubscriptionMessage.chat())
        .order(tagSubscriptionMessage.order())
        .scope(tagSubscriptionMessage.scope())
        .type(tagSubscriptionMessage.type())
        .lastPermalink(tagSubscriptionMessage.coub().getPermalink())
        .build();
  }

  @Override
  public SentCoubsRegistry convertToRegistry(TagSubscriptionMessage tagSubscriptionMessage) {
    return SentCoubsRegistry.builder()
        .subscription(Subscription.builder()
            .id(tagSubscriptionMessage.id())
            .build())
        .coubPermalink(tagSubscriptionMessage.coub().getPermalink())
        .build();
  }

  @Override
  public SentCoubsRegistry convertToRegistry(
      CommunitySubscriptionMessage communitySubscriptionMessage) {
    return SentCoubsRegistry.builder()
        .subscription(Subscription.builder()
            .id(communitySubscriptionMessage.id())
            .build())
        .coubPermalink(communitySubscriptionMessage.coub().getPermalink())
        .build();
  }

}
