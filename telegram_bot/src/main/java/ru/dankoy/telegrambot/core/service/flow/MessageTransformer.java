package ru.dankoy.telegrambot.core.service.flow;

import org.telegram.telegrambots.meta.api.objects.message.Message;

public interface MessageTransformer {

  org.springframework.messaging.Message<Message> addCommandStringToHeaders(
      org.springframework.messaging.Message<Message> message);
}
