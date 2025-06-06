package ru.dankoy.telegrambot.core.service.flow;

import org.telegram.telegrambots.meta.api.objects.message.Message;

public interface ChatFlowHandler {

  Message checkChatStatus(org.springframework.messaging.Message<Message> message);

  Message createChat(Message message);
}
