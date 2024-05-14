package ru.dankoy.telegrambot.core.service.reply;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.dankoy.telegrambot.core.domain.subscription.SubscriptionType;
import ru.dankoy.telegrambot.core.dto.flow.CreateReplyMySubscriptionsDto;
import ru.dankoy.telegrambot.core.dto.flow.CreateReplySubscribeDto;
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
  public SendMessage createReplyCommunitySubscriptionSuccessful(
      CreateReplySubscribeDto createReplySubscribeDto) {

    var inputMessage = createReplySubscribeDto.message();
    var sendMessage = createSendMessage(inputMessage);
    var s = createReplySubscribeDto.communitySubscriptionDto().communitySubscription();

    sendMessage.setText(
        localisationService.getLocalizedMessage(
            TEMPLATE_SUBSCRIPTION_SUCCESS,
            new Object[] {s.getCommunity().getName(), s.getSection().getName()},
            localeProvider.getLocale(inputMessage)));

    return sendMessage;
  }

  @Override
  public SendMessage createReplySubscriptionHelp(MessagingException messagingException) {
    //      send(buildHelpMessage(inputMessage, command.get(COMMAND),
    // TEMPLATE_SUBSCRIPTION_EXCEPTION));

    var message = messagingException.getFailedMessage();
    var inputMessage = (Message) message.getPayload();
    Map<String, String> command =
        (Map<String, String>)
            messagingException.getFailedMessage().getHeaders().get("parsedCommand");

    var sendMessage = createSendMessage(inputMessage);

    Map<String, Object> templateData = new HashMap<>();
    templateData.put(COMMAND, command);

    var text =
        templateBuilder.writeTemplate(
            templateData, TEMPLATE_SUBSCRIPTION_EXCEPTION, localeProvider.getLocale(inputMessage));

    sendMessage.setText(text);

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
    templateData.put(COMMAND, command);

    var text =
        templateBuilder.writeTemplate(
            templateData, helpType, localeProvider.getLocale(inputMessage));

    sendMessage.setText(text);

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
