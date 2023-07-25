package ru.dankoy.telegrambot.core.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.telegrambot.core.domain.message.SubscriptionMessage;
import ru.dankoy.telegrambot.core.service.bot.TelegramBot;

@RequiredArgsConstructor
@RestController
public class SubscriptionController {

  private final TelegramBot telegramBot;

  @PostMapping("/api/v1/message")
  public void sendMessage(@RequestBody SubscriptionMessage message) {
    telegramBot.sendMessage(message);
  }

}
