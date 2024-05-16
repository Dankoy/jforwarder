package ru.dankoy.telegrambot.core.service.reply;

import org.springframework.messaging.MessagingException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.dankoy.telegrambot.core.domain.message.ChannelSubscriptionMessage;
import ru.dankoy.telegrambot.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.telegrambot.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.telegrambot.core.dto.flow.CreateReplyCommunitiesDto;
import ru.dankoy.telegrambot.core.dto.flow.CreateReplyMySubscriptionsDto;
import ru.dankoy.telegrambot.core.dto.flow.CreateReplyOrdersDto;
import ru.dankoy.telegrambot.core.dto.flow.CreateReplySubscribeDto;
import ru.dankoy.telegrambot.core.dto.flow.CreateReplyUnsubscribeDto;

public interface ReplyCreatorService {

  SendMessage createReplyMySubscriptions(
      CreateReplyMySubscriptionsDto createReplyMySubscriptionsDto);

  SendMessage createReplyCommunities(CreateReplyCommunitiesDto createReplyCommunitiesDto);

  SendMessage createReplyOrders(CreateReplyOrdersDto createReplyOrdersDto);

  SendMessage createReplyCommunitySubscriptionSuccessful(
      CreateReplySubscribeDto createReplySubscribeDto);

  SendMessage createReplyTagSubscriptionSuccessful(CreateReplySubscribeDto createReplySubscribeDto);

  SendMessage createReplyChannelSubscriptionSuccessful(
      CreateReplySubscribeDto createReplySubscribeDto);

  SendMessage createReplyUnsubscriptionSuccessful(
      CreateReplyUnsubscribeDto createReplyUnsubscribeDto);

  SendMessage createReplyStart(Message inputMessage);

  SendMessage createReplyHelp(Message inputMessage);

  SendMessage createCommunitySubscriptionMessage(CommunitySubscriptionMessage message);

  SendMessage createTagSubscriptionMessage(TagSubscriptionMessage message);

  SendMessage createChannelSubscriptionMessage(ChannelSubscriptionMessage message);

  SendMessage replyWithMessageSourceOnException(MessagingException messagingException);

  SendMessage replyWithFreemarkerOnException(MessagingException messagingException);

  SendMessage replyWithHelpOnBotCommandFlowException(MessagingException messagingException);
}
