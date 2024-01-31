package ru.dankoy.kafkamessageproducer.core.service.subscription;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.kafkamessageproducer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.communitysubscription.CommunitySubscription;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.tagsubscription.TagSubscription;
import ru.dankoy.kafkamessageproducer.core.feign.subscriptionsholder.subscription.SubscriptionFeign;
import ru.dankoy.kafkamessageproducer.core.service.converter.MessageConverter;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

  private final SubscriptionFeign subscriptionFeign;

  private final MessageConverter messageConverter;


  public CommunitySubscription updateCommunitySubscriptionPermalink(
      CommunitySubscriptionMessage communitySubscriptionMessage) {

    var subscription = messageConverter.convert(communitySubscriptionMessage);

    return subscriptionFeign.updateCommunitySubscriptionPermalink(subscription);

  }

  @Override
  public TagSubscription updateTagSubscriptionPermalink(
      TagSubscriptionMessage tagSubscriptionMessage) {
    var subscription = messageConverter.convert(tagSubscriptionMessage);

    return subscriptionFeign.updateTagSubscriptionPermalink(subscription);
  }

}
