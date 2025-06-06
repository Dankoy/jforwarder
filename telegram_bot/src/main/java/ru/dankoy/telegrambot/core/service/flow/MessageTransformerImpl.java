package ru.dankoy.telegrambot.core.service.flow;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
public class MessageTransformerImpl implements MessageTransformer {

  @Override
  public org.springframework.messaging.Message<Message> addCommandStringToHeaders(
      org.springframework.messaging.Message<Message> message) {

    var botMessage = message.getPayload();
    String[] command = botMessage.getText().split(" ");

    return MessageBuilder.fromMessage(message)
        .copyHeaders(message.getHeaders())
        .setHeader("commandString", command[0])
        .build();
  }
}
