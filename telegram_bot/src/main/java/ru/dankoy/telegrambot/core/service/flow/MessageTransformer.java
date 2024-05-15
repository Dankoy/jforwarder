package ru.dankoy.telegrambot.core.service.flow;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageTransformer {

  org.springframework.messaging.Message<Message> addCommandStringToHeaders(
      org.springframework.messaging.Message<Message> message);
}
