package ru.dankoy.kafkamessageconsumer.core.service.consumer;

import java.util.List;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CoubMessage;

public interface CoubMessageConsumer {

    void accept(List<? extends CoubMessage> value);

    void accept(CoubMessage value);
}
