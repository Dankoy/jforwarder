package ru.dankoy.telegrambot.core.dto.flow;

import org.telegram.telegrambots.meta.api.objects.Message;

public record CreateReplyMySubscriptionsDto(
    Message message, MySubscriptionsDto mySubscriptionsDto) {}
