package ru.dankoy.kafkamessageproducer.core.service.messagesender.protobuf;

import com.google.protobuf.Message;

public interface MessageProducerServiceKafkaProtobuf {

  void send(Message message);
}
