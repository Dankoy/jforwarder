package ru.dankoy.telegrambot.core.dto.flow;

import org.telegram.telegrambots.meta.api.objects.message.Message;

public record CreateReplyUnsubscribeDto(Message message, Object[] objects) {}
