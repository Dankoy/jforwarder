package ru.dankoy.telegramchatservice.core.domain.filter;

public record TelegramChatFilter(
    Long chatId,
    String type,
    String title,
    String firstName,
    String lastName,
    String username,
    Boolean active,
    Long messageThreadId) {}
