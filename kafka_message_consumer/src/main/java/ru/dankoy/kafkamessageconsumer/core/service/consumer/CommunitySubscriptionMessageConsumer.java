package ru.dankoy.kafkamessageconsumer.core.service.consumer;

import java.util.List;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CommunitySubscriptionMessage;

/**
 * @deprecated in favor {@link CoubMessageConsumer}
 */
@Deprecated(since = "2024-02-01")
public interface CommunitySubscriptionMessageConsumer {

    void accept(List<CommunitySubscriptionMessage> value);

    void accept(CommunitySubscriptionMessage value);
}
