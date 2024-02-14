package ru.dankoy.kafkamessageconsumer.core.service.consumer;

import java.util.List;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.listener.BatchListenerFailedException;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CoubMessage;

@Slf4j
@RequiredArgsConstructor
public class CoubMessageConsumerImpl implements CoubMessageConsumer {

    private final Consumer<CoubMessage> telegramBotServiceConsumer;

    @Override
    public void accept(List<? extends CoubMessage> value) {
        int i = 0;
        try {
            for (; i < value.size(); i++) {
                var v = value.get(i);
                log.info("Sending message: {}", v);
                telegramBotServiceConsumer.accept(v);
                log.info("Message sent");
            }
        } catch (Exception e) {
            log.error("Message not sent with error: {}", e.getMessage());
            throw new BatchListenerFailedException("failed", i);
        }
    }

    @Override
    public void accept(CoubMessage value) {
        log.info("Sending message: {}", value);
        telegramBotServiceConsumer.accept(value);
        log.info("Message sent");
    }
}
