package ru.dankoy.kafkamessageconsumer.core.exceptions;

public class KafkaConsumerException extends RuntimeException {

    public KafkaConsumerException(String message, Exception ex) {

        super(message, ex);
    }

    public KafkaConsumerException(String message) {

        super(message);
    }
}
