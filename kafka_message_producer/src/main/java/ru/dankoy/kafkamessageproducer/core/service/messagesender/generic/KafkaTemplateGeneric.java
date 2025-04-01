package ru.dankoy.kafkamessageproducer.core.service.messagesender.generic;

import org.apache.kafka.clients.producer.ProducerRecord;

public interface KafkaTemplateGeneric<K, V> {

  void send(ProducerRecord<K, V> producerRecord);
}
