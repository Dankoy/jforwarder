package ru.dankoy.telegrambot.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.telegrambot.core.domain.message.ChannelSubscriptionMessage;
import ru.dankoy.telegrambot.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.telegrambot.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.telegrambot.core.gateway.BotMessageGateway;

@RequiredArgsConstructor
@RestController
public class SubscriptionController {

  private final BotMessageGateway botMessageGateway;

  @PostMapping("/api/v1/community_message")
  public void sendMessage(@RequestBody CommunitySubscriptionMessage message) {
    botMessageGateway.process(message);
  }

  @PostMapping("/api/v1/tag_message")
  public void sendMessage(@RequestBody TagSubscriptionMessage message) {
    botMessageGateway.process(message);
  }

  @PostMapping("/api/v1/channel_message")
  public void sendMessage(@RequestBody ChannelSubscriptionMessage message) {
    botMessageGateway.process(message);
  }
}
