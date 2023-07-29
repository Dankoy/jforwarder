package ru.dankoy.kafkamessageproducer.core.service.converter;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import ru.dankoy.kafkamessageproducer.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.tagsubscription.TagSubscription;


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
    return new TagSubscription(
        tagSubscriptionMessage.id(),
        tagSubscriptionMessage.tag(),
        tagSubscriptionMessage.chat(),
        tagSubscriptionMessage.order(),
        tagSubscriptionMessage.scope(),
        tagSubscriptionMessage.type(),
        tagSubscriptionMessage.coub().getPermalink(),
        new ArrayList<>()
    );

  }
}
