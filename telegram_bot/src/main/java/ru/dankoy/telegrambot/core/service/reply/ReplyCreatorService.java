package ru.dankoy.telegrambot.core.service.reply;

import org.springframework.messaging.MessagingException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.dankoy.telegrambot.core.dto.flow.CreateReplyMySubscriptionsDto;
import ru.dankoy.telegrambot.core.dto.flow.CreateReplySubscribeDto;

public interface ReplyCreatorService {

  SendMessage createReplyMySubscriptions(
      CreateReplyMySubscriptionsDto createReplyMySubscriptionsDto);

  SendMessage createReplyCommunitySubscriptionSuccessful(
      CreateReplySubscribeDto createReplySubscribeDto);

  SendMessage createReplySubscriptionHelp(MessagingException messagingException);

  SendMessage createReplyStart(Message inputMessage);

  SendMessage createReplyHelp(Message inputMessage);

  SendMessage replyWithMessageSourceOnException(MessagingException messagingException);

  SendMessage replyWithFreemarkerOnException(MessagingException messagingException);
}
