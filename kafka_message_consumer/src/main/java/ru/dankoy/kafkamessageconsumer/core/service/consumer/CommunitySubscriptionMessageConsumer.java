package ru.dankoy.kafkamessageconsumer.core.service.consumer;

import java.util.List;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CommunitySubscriptionMessage;

public interface CommunitySubscriptionMessageConsumer {

    void accept(List<CommunitySubscriptionMessage> value);
}
