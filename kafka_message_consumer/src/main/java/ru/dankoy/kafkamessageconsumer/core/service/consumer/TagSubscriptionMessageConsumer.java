package ru.dankoy.kafkamessageconsumer.core.service.consumer;

import java.util.List;
import ru.dankoy.kafkamessageconsumer.core.domain.message.TagSubscriptionMessage;

/**
 * @deprecated in favor {@link CoubMessageConsumer}
 */
@Deprecated(since = "2024-02-01")
public interface TagSubscriptionMessageConsumer {

    void accept(List<TagSubscriptionMessage> values);

    void accept(TagSubscriptionMessage value);
}
