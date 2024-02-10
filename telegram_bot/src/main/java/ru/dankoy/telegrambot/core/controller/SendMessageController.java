package ru.dankoy.telegrambot.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.telegrambot.core.dto.SendMessageDto;
import ru.dankoy.telegrambot.core.service.bot.TelegramBot;

@RequiredArgsConstructor
@RestController
public class SendMessageController {

  private final TelegramBot telegramBot;

  @PostMapping("/api/v1/send_message")
  public void sendMessage(@RequestBody SendMessageDto dto) {

    var fromDto = SendMessageDto.fromDTO(dto);

    fromDto.forEach(telegramBot::sendMessage);
  }
}
