package ru.dankoy.telegrambot.core.dto.flow;

import java.util.List;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.dankoy.telegrambot.core.domain.subscription.Order;

public record CreateReplyOrdersDto(Message message, List<Order> orders) {}
