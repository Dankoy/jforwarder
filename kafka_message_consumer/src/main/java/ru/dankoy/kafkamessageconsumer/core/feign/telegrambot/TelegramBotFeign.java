package ru.dankoy.kafkamessageconsumer.core.feign.telegrambot;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.dankoy.kafkamessageconsumer.core.domain.message.CoubMessage;

/**
 * @deprecated since spring-boot 4.0.0 in favor {@link ChannelSubscriptionServiceHttpClient}
 */
@Deprecated(since = "2025-01-04", forRemoval = true)
@FeignClient(name = "telegram-bot")
public interface TelegramBotFeign {

  @PostMapping(path = "/api/v1/community_message")
  void sendCoubCommunityMessage(@RequestBody CoubMessage communitySubscriptionMessage);

  @PostMapping(path = "/api/v1/tag_message")
  void sendCoubTagMessage(@RequestBody CoubMessage tagSubscriptionMessage);

  @PostMapping(path = "/api/v1/channel_message")
  void sendCoubChannelMessage(@RequestBody CoubMessage channelSubscriptionMessage);
}
