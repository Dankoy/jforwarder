package ru.dankoy.telegrambot.core.service.flow;

import org.telegram.telegrambots.meta.api.objects.message.Message;

public interface BotMessageRouter {

  org.springframework.messaging.Message<Message> commandRoute(Message message);
}
