package ru.dankoy.kafkamessageconsumer.core.service.consumer;

import ru.dankoy.kafkamessageconsumer.core.domain.coub.Coub;
import ru.dankoy.kafkamessageconsumer.core.domain.message.ChannelSubscriptionMessage;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CoubMessage;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Chat;

public interface MessageMaker {

  default CoubMessage makeMessage() {

    return ChannelSubscriptionMessage.builder().coub(new Coub()).id(1L).chat(new Chat()).build();
  }
}
