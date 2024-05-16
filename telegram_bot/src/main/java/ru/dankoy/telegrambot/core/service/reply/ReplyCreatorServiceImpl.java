package ru.dankoy.telegrambot.core.service.reply;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.dankoy.telegrambot.core.domain.subscription.SubscriptionType;
import ru.dankoy.telegrambot.core.domain.subscription.channel.ChannelSubscription;
import ru.dankoy.telegrambot.core.domain.subscription.community.CommunitySubscription;
import ru.dankoy.telegrambot.core.domain.subscription.tag.TagSubscription;
import ru.dankoy.telegrambot.core.dto.flow.CreateReplyCommunitiesDto;
import ru.dankoy.telegrambot.core.dto.flow.CreateReplyMySubscriptionsDto;
import ru.dankoy.telegrambot.core.dto.flow.CreateReplySubscribeDto;
import ru.dankoy.telegrambot.core.dto.flow.CreateReplyUnsubscribeDto;
import ru.dankoy.telegrambot.core.exceptions.BotCommandFlowException;
import ru.dankoy.telegrambot.core.exceptions.BotFlowException;
import ru.dankoy.telegrambot.core.service.localeprovider.LocaleProvider;
import ru.dankoy.telegrambot.core.service.localization.LocalisationService;
import ru.dankoy.telegrambot.core.service.template.TemplateBuilder;

@RequiredArgsConstructor
@Service
public class ReplyCreatorServiceImpl implements ReplyCreatorService {

  private static final String COMMAND = "command";
  private static final String TEMPLATE_SUBSCRIPTION_SUCCESS = "subscriptionCompleted";
  private static final String TEMPLATE_SUBSCRIPTION_EXCEPTION = "subscription_exception.ftl";
  private static final String TEMPLATE_UNSUBSCRIBE_SUCCESS = "unsubscriptionCompleted";

  private final LocalisationService localisationService;
  private final TemplateBuilder templateBuilder;
  private final LocaleProvider localeProvider;

  @Override
  public SendMessage createReplyMySubscriptions(
      CreateReplyMySubscriptionsDto createReplyMySubscriptionsDto) {

    var inputMessage = createReplyMySubscriptionsDto.message();
    var sendMessage = createSendMessage(inputMessage);

    Map<String, Object> templateData = new HashMap<>();
    templateData.put(
        "communitySubscriptions",
        createReplyMySubscriptionsDto.mySubscriptionsDto().communitySubscriptions());
    templateData.put(
        "tagSubscriptions", createReplyMySubscriptionsDto.mySubscriptionsDto().tagSubscriptions());
    templateData.put(
        "channelSubscriptions",
        createReplyMySubscriptionsDto.mySubscriptionsDto().channelSubscriptions());

    var text =
        templateBuilder.writeTemplate(
            templateData, "subscriptions.ftl", localeProvider.getLocale(inputMessage));

    sendMessage.setText(text);

    return sendMessage;
  }

  @Override
  public SendMessage createReplyCommunities(CreateReplyCommunitiesDto createReplyCommunitiesDto) {

    var inputMessage = createReplyCommunitiesDto.message();
    var communities = createReplyCommunitiesDto.communities();
    var sendMessage = createSendMessage(inputMessage);

    Map<String, Object> templateData = new HashMap<>();
    templateData.put("communities", communities);

    var text =
        templateBuilder.writeTemplate(
            templateData, "communities.ftl", localeProvider.getLocale(inputMessage));

    sendMessage.setText(text);
    sendMessage.setParseMode(ParseMode.MARKDOWN);

    return sendMessage;
  }

  @Override
  public SendMessage createReplyCommunitySubscriptionSuccessful(
      CreateReplySubscribeDto createReplySubscribeDto) {

    var inputMessage = createReplySubscribeDto.message();
    var sendMessage = createSendMessage(inputMessage);
    var s = (CommunitySubscription) createReplySubscribeDto.subscriptionDto().subscription();

    sendMessage.setText(
        localisationService.getLocalizedMessage(
            TEMPLATE_SUBSCRIPTION_SUCCESS,
            new Object[] {s.getCommunity().getName(), s.getSection().getName()},
            localeProvider.getLocale(inputMessage)));

    return sendMessage;
  }

  @Override
  public SendMessage createReplyTagSubscriptionSuccessful(
      CreateReplySubscribeDto createReplySubscribeDto) {

    var inputMessage = createReplySubscribeDto.message();
    var sendMessage = createSendMessage(inputMessage);
    var s = (TagSubscription) createReplySubscribeDto.subscriptionDto().subscription();

    sendMessage.setText(
        localisationService.getLocalizedMessage(
            TEMPLATE_SUBSCRIPTION_SUCCESS,
            new Object[] {s.getTag().getTitle(), s.getOrder().getName()},
            localeProvider.getLocale(inputMessage)));

    return sendMessage;
  }

  @Override
  public SendMessage createReplyChannelSubscriptionSuccessful(
      CreateReplySubscribeDto createReplySubscribeDto) {

    var inputMessage = createReplySubscribeDto.message();
    var sendMessage = createSendMessage(inputMessage);
    var s = (ChannelSubscription) createReplySubscribeDto.subscriptionDto().subscription();

    sendMessage.setText(
        localisationService.getLocalizedMessage(
            TEMPLATE_SUBSCRIPTION_SUCCESS,
            new Object[] {s.getChannel().getPermalink(), s.getOrder().getName()},
            localeProvider.getLocale(inputMessage)));

    return sendMessage;
  }

  @Override
  public SendMessage createReplyUnsubscriptionSuccessful(
      CreateReplyUnsubscribeDto createReplyUnsubscribeDto) {

    var inputMessage = createReplyUnsubscribeDto.message();
    var sendMessage = createSendMessage(inputMessage);
    var objects = createReplyUnsubscribeDto.objects();

    sendMessage.setText(
        localisationService.getLocalizedMessage(
            TEMPLATE_UNSUBSCRIBE_SUCCESS, objects, localeProvider.getLocale(inputMessage)));

    return sendMessage;
  }

  @Override
  public SendMessage createReplyStart(Message inputMessage) {

    var sendMessage = createSendMessage(inputMessage);

    sendMessage.setText(
        localisationService.getLocalizedMessage(
            "startFinish", null, localeProvider.getLocale(inputMessage)));

    return sendMessage;
  }

  @Override
  public SendMessage createReplyHelp(Message inputMessage) {

    var sendMessage = createSendMessage(inputMessage);

    Map<String, Object> templateData = new HashMap<>();
    templateData.put("subscription_types", Arrays.toString(SubscriptionType.values()));

    var text =
        templateBuilder.writeTemplate(
            templateData, "help.ftl", localeProvider.getLocale(inputMessage));
    sendMessage.setText(text);

    return sendMessage;
  }

  @Override
  public SendMessage replyWithMessageSourceOnException(MessagingException messagingException) {

    var botException = (BotFlowException) messagingException.getCause();
    var inputMessage = botException.getInputBotMessage();

    var sendMessage = createSendMessage(inputMessage);

    sendMessage.setText(
        localisationService.getLocalizedMessage(
            botException.getLocalizedSourceMessage(),
            botException.getObjects(),
            localeProvider.getLocale(inputMessage)));

    return sendMessage;
  }

  @Override
  public SendMessage replyWithFreemarkerOnException(MessagingException messagingException) {

    var botException = (BotCommandFlowException) messagingException.getCause();
    var inputMessage = botException.getInputBotMessage();
    var command = botException.getCommand();
    var helpType = botException.getFreemarkerTemplateName();

    var sendMessage = createSendMessage(inputMessage);

    Map<String, Object> templateData = new HashMap<>();
    templateData.put(COMMAND, command[0]);

    var text =
        templateBuilder.writeTemplate(
            templateData, helpType, localeProvider.getLocale(inputMessage));

    sendMessage.setText(text);
    sendMessage.enableMarkdown(true);

    return sendMessage;
  }

  @Override
  public SendMessage replyWithHelpOnBotCommandFlowException(MessagingException messagingException) {

    var botException = (BotCommandFlowException) messagingException.getCause();
    var message = messagingException.getFailedMessage();
    var inputMessage = botException.getInputBotMessage();
    return helpReply(message, inputMessage);
  }

  private SendMessage helpReply(
      org.springframework.messaging.Message<?> message, Message inputMessage) {

    var command = message.getHeaders().get("commandString");

    var sendMessage = createSendMessage(inputMessage);

    Map<String, Object> templateData = new HashMap<>();
    templateData.put(COMMAND, command);

    var text =
        templateBuilder.writeTemplate(
            templateData, TEMPLATE_SUBSCRIPTION_EXCEPTION, localeProvider.getLocale(inputMessage));

    sendMessage.setText(text);
    sendMessage.enableMarkdown(true);

    return sendMessage;
  }

  private SendMessage createSendMessage(Message inputMessage) {
    var sendMessage = new SendMessage();
    sendMessage.setChatId(inputMessage.getChat().getId());
    sendMessage.setMessageThreadId(inputMessage.getMessageThreadId());
    sendMessage.setReplyToMessageId(inputMessage.getMessageId());
    return sendMessage;
  }
}
