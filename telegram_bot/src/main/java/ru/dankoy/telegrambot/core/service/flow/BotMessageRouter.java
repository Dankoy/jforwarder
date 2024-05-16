package ru.dankoy.telegrambot.core.service.flow;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface BotMessageRouter {

  org.springframework.messaging.Message<Message> commandRoute(Message message);
}
