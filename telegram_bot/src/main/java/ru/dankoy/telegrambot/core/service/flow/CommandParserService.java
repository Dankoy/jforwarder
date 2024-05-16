package ru.dankoy.telegrambot.core.service.flow;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface CommandParserService {

  org.springframework.messaging.Message<Message> parseSubscribeCommand(Message message);

  org.springframework.messaging.Message<Message> parseOrderCommandMultipleWords(
      Message inputMessage);
}
