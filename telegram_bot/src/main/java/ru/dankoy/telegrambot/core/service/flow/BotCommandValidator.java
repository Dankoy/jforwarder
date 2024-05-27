package ru.dankoy.telegrambot.core.service.flow;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface BotCommandValidator {

  org.springframework.messaging.Message<Message> isValid(Message message);
}
