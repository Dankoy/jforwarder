package ru.dankoy.telegramchatservice.core.domain.filter;

import java.util.UUID;

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
