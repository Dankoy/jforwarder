package ru.dankoy.kafkamessageproducer.core.service.messagesender.generic;

import ru.dankoy.kafkamessageproducer.core.domain.coub.Coub;
import ru.dankoy.kafkamessageproducer.core.domain.message.ChannelSubscriptionMessage;
import ru.dankoy.kafkamessageproducer.core.domain.message.CoubMessage;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Chat;

public interface MessageMaker {

  default CoubMessage makeMessage() {

    return ChannelSubscriptionMessage.builder().coub(new Coub()).id(1L).chat(new Chat()).build();
  }
}
