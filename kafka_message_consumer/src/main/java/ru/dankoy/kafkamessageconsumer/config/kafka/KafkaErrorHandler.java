package ru.dankoy.kafkamessageconsumer.config.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.errors.RecordDeserializationException;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.lang.Nullable;
import org.springframework.util.backoff.BackOff;

@Slf4j
public class KafkaErrorHandler extends DefaultErrorHandler {

  public KafkaErrorHandler() {
    super();
  }

  public KafkaErrorHandler(@Nullable ConsumerRecordRecoverer recoverer, BackOff backOff) {
    super(recoverer, backOff, null);
  }

  @Override
  public void handleOtherException(
      Exception thrownException,
      Consumer<?, ?> consumer,
      MessageListenerContainer container,
      boolean batchListener) {
    log.error("Exception thrown", thrownException);
    if (thrownException instanceof RecordDeserializationException ex) {
      consumer.seek(ex.topicPartition(), ex.offset() + 1L);
      consumer.commitSync();
    } else {
      log.error("Exception not handled", thrownException);
    }
  }
}
