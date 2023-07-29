package ru.dankoy.kafkamessageproducer.core.service.subscription;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.kafkamessageproducer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.CommunitySubscription;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.tagsubscription.TagSubscription;
import ru.dankoy.kafkamessageproducer.core.feign.subscriptionsholder.subscription.SubscriptionFeign;
import ru.dankoy.kafkamessageproducer.core.service.converter.CommunityMessageConverter;
import ru.dankoy.kafkamessageproducer.core.service.converter.TagMessageConverter;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

  private final SubscriptionFeign subscriptionFeign;

  private final CommunityMessageConverter communityMessageConverter;

  private final TagMessageConverter tagMessageConverter;


  public CommunitySubscription updateCommunitySubscriptionPermalink(
      CommunitySubscriptionMessage communitySubscriptionMessage) {

    var subscription = communityMessageConverter.convert(communitySubscriptionMessage);

    return subscriptionFeign.updateCommunitySubscriptionPermalink(subscription);

  }

  @Override
  public TagSubscription updateTagSubscriptionPermalink(
      TagSubscriptionMessage tagSubscriptionMessage) {
    var subscription = tagMessageConverter.convert(tagSubscriptionMessage);

    return subscriptionFeign.updateTagSubscriptionPermalink(subscription);
  }

}
