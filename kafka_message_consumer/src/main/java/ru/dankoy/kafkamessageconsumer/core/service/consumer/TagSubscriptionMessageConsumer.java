package ru.dankoy.kafkamessageconsumer.core.service.consumer;

import java.util.List;
import ru.dankoy.kafkamessageconsumer.core.domain.message.TagSubscriptionMessage;

public interface TagSubscriptionMessageConsumer {

  void accept(List<TagSubscriptionMessage> values);

  void accept(TagSubscriptionMessage value);
}
