package ru.dankoy.kafkamessageproducer.core.service.converter;

import java.util.List;
import org.springframework.stereotype.Component;
import ru.dankoy.kafkamessageproducer.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.tagsubscription.TagSubscription;


/**
 * @deprecated use {@link MessageConverter}
 */
@Deprecated(since = "2024-01-30")
@Component
public class TagMessageConverterImpl implements TagMessageConverter {

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
}
