package ru.dankoy.telegrambot.core.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.dankoy.telegrambot.core.domain.message.CoubMessage;

@MessagingGateway(errorChannel = "errorChannel")
public interface BotMessageGateway {

  @Gateway(requestChannel = "inputMessageChannel")
  void process(Message telegramMessage);

  @Gateway(requestChannel = "subscriptionMessagesChannel")
  void process(CoubMessage communitySubscriptionMessage);
}
