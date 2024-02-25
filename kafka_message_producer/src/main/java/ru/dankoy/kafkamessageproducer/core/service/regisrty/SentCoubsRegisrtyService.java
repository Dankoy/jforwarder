package ru.dankoy.kafkamessageproducer.core.service.regisrty;

import ru.dankoy.kafkamessageproducer.core.domain.message.CoubMessage;
import ru.dankoy.kafkamessageproducer.core.domain.registry.SentCoubsRegistry;

public interface SentCoubsRegisrtyService {

  //  SentCoubsRegistry create(
  //      CommunitySubscriptionMessage communitySubscriptionMessage);
  //
  //  SentCoubsRegistry create(
  //      TagSubscriptionMessage tagSubscriptionMessage);
  //
  //  SentCoubsRegistry create(
  //      ChannelSubscriptionMessage channelSubscriptionMessage);

  SentCoubsRegistry create(CoubMessage coubMessage);
}
