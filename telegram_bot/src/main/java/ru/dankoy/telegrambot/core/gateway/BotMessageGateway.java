package ru.dankoy.telegrambot.core.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@MessagingGateway(errorChannel = "errorChannel")
public interface BotMessageGateway {

  @Gateway(requestChannel = "inputMessageChannel")
  void process(Message telegramMessage);
}
