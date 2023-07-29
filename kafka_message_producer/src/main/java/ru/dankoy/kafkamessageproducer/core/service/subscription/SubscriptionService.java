package ru.dankoy.kafkamessageproducer.core.service.subscription;

import ru.dankoy.kafkamessageproducer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.CommunitySubscription;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.tagsubscription.TagSubscription;

public interface SubscriptionService {

  CommunitySubscription updateCommunitySubscriptionPermalink(
      CommunitySubscriptionMessage subscription);

  TagSubscription updateTagSubscriptionPermalink(
      TagSubscriptionMessage subscription);

}
