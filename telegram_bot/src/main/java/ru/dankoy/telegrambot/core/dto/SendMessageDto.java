package ru.dankoy.telegrambot.core.dto;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@RequiredArgsConstructor
public class SendMessageDto {

  private final Set<Long> chatIds;
  private final String message;

  public static Set<SendMessage> fromDTO(SendMessageDto dto) {

    return dto.chatIds.stream()
        .map(id -> SendMessage.builder().chatId(id).text(dto.message).build())
        .collect(Collectors.toSet());
  }
}
