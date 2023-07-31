package ru.dankoy.kafkamessageconsumer.core.feign.telegrambot;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CommunitySubscriptionMessage;

@FeignClient(name = "telegram-bot")
public interface TelegramBotFeign {


  @PostMapping(path = "/api/v1/message")
  void send(@RequestBody CommunitySubscriptionMessage communitySubscriptionMessage);

}
