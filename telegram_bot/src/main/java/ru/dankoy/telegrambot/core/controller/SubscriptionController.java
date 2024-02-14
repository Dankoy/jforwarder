package ru.dankoy.telegrambot.core.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.telegrambot.core.domain.message.ChannelSubscriptionMessage;
import ru.dankoy.telegrambot.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.telegrambot.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.telegrambot.core.service.bot.TelegramBot;

@RequiredArgsConstructor
@RestController
public class SubscriptionController {

  private final TelegramBot telegramBot;

  @PostMapping("/api/v1/community_message")
  public void sendMessage(@RequestBody CommunitySubscriptionMessage message) {
    telegramBot.sendMessage(message);
  }

  @PostMapping("/api/v1/tag_message")
  public void sendMessage(@RequestBody TagSubscriptionMessage message) {
    telegramBot.sendMessage(message);
  }

  @PostMapping("/api/v1/channel_message")
  public void sendMessage(@RequestBody ChannelSubscriptionMessage message) {
    telegramBot.sendMessage(message);
  }

}
