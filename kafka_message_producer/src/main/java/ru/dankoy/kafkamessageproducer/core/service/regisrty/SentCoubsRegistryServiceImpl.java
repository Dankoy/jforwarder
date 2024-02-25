package ru.dankoy.kafkamessageproducer.core.service.regisrty;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.kafkamessageproducer.core.domain.message.CoubMessage;
import ru.dankoy.kafkamessageproducer.core.domain.registry.SentCoubsRegistry;
import ru.dankoy.kafkamessageproducer.core.feign.subscriptionsholder.subscription.SubscriptionFeign;
import ru.dankoy.kafkamessageproducer.core.service.converter.MessageConverter;

@Service
@RequiredArgsConstructor
public class SentCoubsRegistryServiceImpl implements SentCoubsRegisrtyService {

  private final SubscriptionFeign subscriptionFeign;

  private final MessageConverter messageConverter;

  //  @Override
  //  public SentCoubsRegistry create(CommunitySubscriptionMessage communitySubscriptionMessage) {
  //
  //    var subscription = messageConverter.convertToRegistry(communitySubscriptionMessage);
  //
  //    return subscriptionFeign.createRegistryEntry(subscription);
  //
  //  }
  //
  //  @Override
  //  public SentCoubsRegistry create(TagSubscriptionMessage tagSubscriptionMessage) {
  //
  //    var subscription = messageConverter.convertToRegistry(tagSubscriptionMessage);
  //    return subscriptionFeign.createRegistryEntry(subscription);
  //  }
  //
  //  @Override
  //  public SentCoubsRegistry create(ChannelSubscriptionMessage channelSubscriptionMessage) {
  //    var subscription = messageConverter.convertToRegistry(channelSubscriptionMessage);
  //    return subscriptionFeign.createRegistryEntry(subscription);
  //  }

  @Override
  public SentCoubsRegistry create(CoubMessage coubMessage) {
    var subscription = messageConverter.convertToRegistry(coubMessage);
    return subscriptionFeign.createRegistryEntry(subscription);
  }
}
