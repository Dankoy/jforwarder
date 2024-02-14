package ru.dankoy.kafkamessageproducer.core.service.messagesender;

import org.apache.kafka.clients.producer.ProducerRecord;
import ru.dankoy.kafkamessageproducer.core.domain.message.CoubMessage;

public interface KafkaTemplateCoubMessage {

    void send(ProducerRecord<String, CoubMessage> producerRecord);
}
