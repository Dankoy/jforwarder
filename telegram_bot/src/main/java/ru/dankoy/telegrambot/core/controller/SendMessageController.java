package ru.dankoy.telegrambot.core.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.telegrambot.core.dto.SendMessageDto;
import ru.dankoy.telegrambot.core.service.bot.TelegramClientService;

@RequiredArgsConstructor
@RestController
public class SendMessageController {

  private final TelegramClientService telegramBot;

  @PostMapping("/api/v1/send_message")
  public void sendMessage(@Valid @RequestBody SendMessageDto dto) {

    var fromDto = SendMessageDto.fromDTO(dto);

    fromDto.forEach(telegramBot::sendMessage);
  }
}
