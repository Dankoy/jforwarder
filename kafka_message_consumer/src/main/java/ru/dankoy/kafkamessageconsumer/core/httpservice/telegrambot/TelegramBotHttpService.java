package ru.dankoy.kafkamessageconsumer.core.httpservice.telegrambot;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CoubMessage;

@HttpExchange(url = "http://telegram-bot")
public interface TelegramBotHttpService {

  @PostExchange(url = "/api/v1/community_message")
  void sendCoubCommunityMessage(@RequestBody CoubMessage communitySubscriptionMessage);

  @PostExchange(url = "/api/v1/tag_message")
  void sendCoubTagMessage(@RequestBody CoubMessage tagSubscriptionMessage);

  @PostExchange(url = "/api/v1/channel_message")
  void sendCoubChannelMessage(@RequestBody CoubMessage channelSubscriptionMessage);
}
