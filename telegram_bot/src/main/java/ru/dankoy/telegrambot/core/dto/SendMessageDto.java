package ru.dankoy.telegrambot.core.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.dankoy.telegrambot.core.dto.validator.ParseModeConstraint;

@RequiredArgsConstructor
public class SendMessageDto {

    @NotNull(message = "chatIds {jakarta.validation.constraints.NotNull.message}")
    @NotEmpty(message = "chatIds {jakarta.validation.constraints.NotNull.message}")
    private final Set<Long> chatIds;

    @NotNull(message = "message {jakarta.validation.constraints.NotNull.message}")
    @NotEmpty(message = "message {jakarta.validation.constraints.NotNull.message}")
    private final String message;

    @ParseModeConstraint(message = "parseMode is invalid")
    private final String parseMode;

    public static Set<SendMessage> fromDTO(SendMessageDto dto) {

        return dto.chatIds.stream()
                .map(
                        id ->
                                SendMessage.builder()
                                        .chatId(id)
                                        .text(dto.message)
                                        .parseMode(dto.parseMode)
                                        .build())
                .collect(Collectors.toSet());
    }
}
