package ru.dankoy.telegramchatservice.core.specifications.telegramchat.filter;

public record TelegramChatFilter(
    Long chatId,
    String type,
    String title,
    String firstName,
    String lastName,
    String username,
    Boolean active,
    Long messageThreadId) {}
