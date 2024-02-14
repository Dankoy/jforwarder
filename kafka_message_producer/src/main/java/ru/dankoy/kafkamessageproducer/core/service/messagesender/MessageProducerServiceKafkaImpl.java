package ru.dankoy.kafkamessageproducer.core.service.messagesender;

import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.dankoy.kafkamessageproducer.core.domain.message.CoubMessage;

@Slf4j
@RequiredArgsConstructor
public class MessageProducerServiceKafkaImpl implements MessageProducerServiceKafka {

    private final String topic;

    private final KafkaTemplateCoubMessage kafkaTemplateCoubMessage;

    private final Consumer<ProducerRecord<String, CoubMessage>> recordConsumer;

    @Override
    public void send(CoubMessage message) {

        try {

            log.info("message: {}", message);

            ProducerRecord<String, CoubMessage> producerRecord =
                    new ProducerRecord<>(topic, message);

            recordConsumer.accept(producerRecord);

            kafkaTemplateCoubMessage.send(producerRecord);

        } catch (Exception e) {
            log.error("send error, value:{}", message, e);
        }
    }
}
