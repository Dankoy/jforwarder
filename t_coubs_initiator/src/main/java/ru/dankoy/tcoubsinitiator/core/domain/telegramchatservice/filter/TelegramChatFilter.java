package ru.dankoy.tcoubsinitiator.core.domain.telegramchatservice.filter;

import java.util.UUID;
import lombok.Builder;

@Builder
public record TelegramChatFilter(
    UUID id,
    Long chatId,
    String type,
    String title,
    String firstName,
    String lastName,
    String username,
    Boolean active,
    Integer messageThreadId) {}
