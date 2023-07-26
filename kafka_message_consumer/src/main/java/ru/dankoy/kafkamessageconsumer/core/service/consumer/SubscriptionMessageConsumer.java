package ru.dankoy.kafkamessageconsumer.core.service.consumer;

import java.util.List;
import ru.dankoy.kafkamessageconsumer.core.domain.message.SubscriptionMessage;

public interface SubscriptionMessageConsumer {

    void accept(List<SubscriptionMessage> value);
}
