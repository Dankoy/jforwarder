package ru.dankoy.telegrambot.core.dto.flow;

import java.util.List;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.dankoy.telegrambot.core.domain.subscription.community.Community;

public record CreateReplyCommunitiesDto(Message message, List<Community> communities) {}
