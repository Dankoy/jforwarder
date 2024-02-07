package ru.dankoy.kafkamessageproducer.core.service.converter;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;
import ru.dankoy.kafkamessageproducer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.message.CoubMessage;
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
        .map(
            c ->
                (CommunitySubscriptionMessage)
                    CommunitySubscriptionMessage.builder()
                        .id(communitySubscription.getId())
                        .chat(communitySubscription.getChat())
                        .coub(c)
                        .community(communitySubscription.getCommunity())
                        .section(communitySubscription.getSection())
                        .lastPermalink(communitySubscription.getLastPermalink())
                        .build())
        .toList();
  }

  @Override
  public List<TagSubscriptionMessage> convert(TagSubscription tagSubscription) {
    return tagSubscription.getCoubs().stream()
        .map(
            c ->
                (TagSubscriptionMessage)
                    TagSubscriptionMessage.builder()
                        .id(tagSubscription.getId())
                        .tag(tagSubscription.getTag())
                        .chat(tagSubscription.getChat())
                        .order(tagSubscription.getOrder())
                        .scope(tagSubscription.getScope())
                        .type(tagSubscription.getType())
                        .coub(c)
                        .lastPermalink(c.getPermalink())
                        .build())
        .toList();
  }

  @Override
  public SentCoubsRegistry convertToRegistry(CoubMessage message) {
    return SentCoubsRegistry.builder()
        .subscription(Subscription.builder().id(message.getId()).build())
        .coubPermalink(message.getCoub().getPermalink())
        .dateTime(LocalDateTime.now())
        .build();
  }

  @Override
  public Subscription convert(CoubMessage message) {

    return Subscription.builder()
        .id(message.getId())
        .chat(message.getChat())
        .lastPermalink(message.getCoub().getPermalink())
        .build();
  }
}
