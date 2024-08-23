package ru.dankoy.telegrambot.core.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.dankoy.telegrambot.core.domain.message.CoubMessage;

// To make sync gateway all their channels must be sync (direct channel), not executors, also don't
// include errorChannel name here

@MessagingGateway
public interface BotMessageSyncGateway {

  @Gateway(requestChannel = "subscriptionMessagesChannel")
  void process(CoubMessage communitySubscriptionMessage);
}
