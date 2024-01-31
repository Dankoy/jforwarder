package ru.dankoy.kafkamessageproducer.core.service.regisrty;

import ru.dankoy.kafkamessageproducer.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.regisrty.SentCoubsRegistry;

public interface SentCoubsRegisrtyService {

  SentCoubsRegistry create(
      CommunitySubscriptionMessage communitySubscriptionMessage);

  SentCoubsRegistry create(
      TagSubscriptionMessage tagSubscriptionMessage);
}
