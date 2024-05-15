package ru.dankoy.telegrambot.core.service.bot.commands;

import feign.FeignException.Conflict;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import ru.dankoy.telegrambot.core.domain.subscription.SubscriptionType;
import ru.dankoy.telegrambot.core.dto.flow.CommunitySubscriptionDto;
import ru.dankoy.telegrambot.core.dto.flow.CreateReplySubscribeDto;
import ru.dankoy.telegrambot.core.exceptions.BotCommandFlowException;
import ru.dankoy.telegrambot.core.exceptions.BotException;
import ru.dankoy.telegrambot.core.exceptions.BotFlowException;
import ru.dankoy.telegrambot.core.exceptions.NotFoundException;
import ru.dankoy.telegrambot.core.service.flow.CommandConstraints;
import ru.dankoy.telegrambot.core.service.subscription.ChannelSubscriptionService;
import ru.dankoy.telegrambot.core.service.subscription.CommunitySubscriptionService;
import ru.dankoy.telegrambot.core.service.subscription.TagSubscriptionService;

@Slf4j
public class SubscribeCommand extends BotCommand {

  private static final String TEMPLATE_SUBSCRIPTION_SUCCESS = "subscriptionCompleted";
  private static final String TEMPLATE_SUBSCRIPTION_EXISTS = "alreadySubscribed";
  private static final String TEMPLATE_SUBSCRIPTION_EXCEPTION = "subscription_exception.ftl";

  private final CommunitySubscriptionService communitySubscriptionService;
  private final TagSubscriptionService tagSubscriptionService;
  private final ChannelSubscriptionService channelSubscriptionService;

  public SubscribeCommand(
      String command,
      String description,
      CommunitySubscriptionService communitySubscriptionService,
      TagSubscriptionService tagSubscriptionService,
      ChannelSubscriptionService channelSubscriptionService) {
    super(command, description);
    this.communitySubscriptionService = communitySubscriptionService;
    this.tagSubscriptionService = tagSubscriptionService;
    this.channelSubscriptionService = channelSubscriptionService;
  }

  public Message subscribe(org.springframework.messaging.Message<Message> message) {

    // find route by first word after command

    var inputMessage = message.getPayload();

    Map<String, String> command = (Map<String, String>) message.getHeaders().get("parsedCommand");

    // route
    assert command != null;

    if (command
        .get(CommandConstraints.COMMAND_SUBSCRIPTION_TYPE_FIELD.getConstraint())
        .equals(SubscriptionType.TAG.getType())) {

      //        subscribeByTag(command, inputMessage);

    } else if (command
        .get(CommandConstraints.COMMAND_SUBSCRIPTION_TYPE_FIELD.getConstraint())
        .equals(SubscriptionType.COMMUNITY.getType())) {

      subscribeToCommunity(command, inputMessage);

    } else if (command
        .get(CommandConstraints.COMMAND_SUBSCRIPTION_TYPE_FIELD.getConstraint())
        .equals(SubscriptionType.CHANNEL.getType())) {

      //        subscribeByChannel(command, inputMessage);
    }

    return inputMessage;
  }

  private CreateReplySubscribeDto subscribeToCommunity(
      Map<String, String> command, Message inputMessage) {

    var communityName = command.get(CommandConstraints.COMMAND_FIRST_FIELD.getConstraint());
    var sectionName = command.get(CommandConstraints.COMMAND_SECOND_FIELD.getConstraint());

    try {

      var s =
          communitySubscriptionService.subscribe(
              communityName,
              sectionName,
              inputMessage.getChat().getId(),
              inputMessage.getMessageThreadId());

      return new CreateReplySubscribeDto(inputMessage, new CommunitySubscriptionDto(s));

    } catch (Conflict e) {

      throw new BotFlowException(
          e.getMessage(),
          inputMessage,
          TEMPLATE_SUBSCRIPTION_EXISTS,
          new Object[] {communityName, sectionName});

    } catch (NotFoundException e) {

      throw new BotFlowException(
          e.getMessage(),
          e,
          inputMessage,
          e.getExceptionObjectType().getType(),
          new Object[] {e.getValue()});

      // todo: надо сделать во флоу так, что бы печаталось еще сообщение кастомное с хелпом

      //      send(buildHelpMessage(inputMessage, command.get(COMMAND),
      // TEMPLATE_SUBSCRIPTION_EXCEPTION));
    } catch (BotException e) {

      throw new BotCommandFlowException(
          e.getMessage(),
          inputMessage,
          TEMPLATE_SUBSCRIPTION_EXCEPTION,
          new String[] {command.get(CommandConstraints.COMMAND.getConstraint())});

      //      send(buildHelpMessage(inputMessage, command.get(COMMAND),
      // TEMPLATE_SUBSCRIPTION_EXCEPTION));
    }
  }
  //
  //  private void subscribeByTag(Map<String, String> command, Message inputMessage) {
  //
  //    var sendMessage = createReply(inputMessage);
  //
  //    var tagName = command.get(COMMAND_FIRST_FIELD);
  //    var orderValue = command.get(COMMAND_SECOND_FIELD);
  //
  //    try {
  //
  //      var s =
  //          tagSubscriptionService.subscribe(
  //              tagName,
  //              orderValue,
  //              "all",
  //              "",
  //              inputMessage.getChat().getId(),
  //              inputMessage.getMessageThreadId());
  //
  //      sendMessage.setText(
  //          localisationService.getLocalizedMessage(
  //              TEMPLATE_SUBSCRIPTION_SUCCESS,
  //              new Object[] {s.getTag().getTitle(), s.getOrder().getValue()},
  //              localeProvider.getLocale(inputMessage)));
  //      send(sendMessage);
  //
  //    } catch (Conflict e) {
  //      sendMessage.setText(
  //          localisationService.getLocalizedMessage(
  //              TEMPLATE_SUBSCRIPTION_EXISTS,
  //              new Object[] {tagName, orderValue},
  //              localeProvider.getLocale(inputMessage)));
  //
  //      send(sendMessage);
  //    } catch (NotFoundException e) {
  //      sendMessage.setText(
  //          localisationService.getLocalizedMessage(
  //              e.getExceptionObjectType().getType(),
  //              new Object[] {e.getValue()},
  //              localeProvider.getLocale(inputMessage)));
  //      send(sendMessage);
  //      send(buildHelpMessage(inputMessage, command.get(COMMAND),
  // TEMPLATE_SUBSCRIPTION_EXCEPTION));
  //    } catch (BotException e) {
  //      send(buildHelpMessage(inputMessage, command.get(COMMAND),
  // TEMPLATE_SUBSCRIPTION_EXCEPTION));
  //    }
  //  }
  //
  //  private void subscribeByChannel(Map<String, String> command, Message inputMessage) {
  //
  //    var sendMessage = createReply(inputMessage);
  //
  //    var channelPermalink = command.get(COMMAND_FIRST_FIELD);
  //    var orderValue = command.get(COMMAND_SECOND_FIELD);
  //
  //    try {
  //
  //      var s =
  //          channelSubscriptionService.subscribe(
  //              channelPermalink,
  //              orderValue,
  //              "all",
  //              "",
  //              inputMessage.getChat().getId(),
  //              inputMessage.getMessageThreadId());
  //
  //      sendMessage.setText(
  //          localisationService.getLocalizedMessage(
  //              TEMPLATE_SUBSCRIPTION_SUCCESS,
  //              new Object[] {s.getChannel().getPermalink(), s.getOrder().getValue()},
  //              localeProvider.getLocale(inputMessage)));
  //      send(sendMessage);
  //
  //    } catch (Conflict e) {
  //      sendMessage.setText(
  //          localisationService.getLocalizedMessage(
  //              TEMPLATE_SUBSCRIPTION_EXISTS,
  //              new Object[] {channelPermalink, orderValue},
  //              localeProvider.getLocale(inputMessage)));
  //
  //      send(sendMessage);
  //    } catch (NotFoundException e) {
  //      sendMessage.setText(
  //          localisationService.getLocalizedMessage(
  //              e.getExceptionObjectType().getType(),
  //              new Object[] {e.getValue()},
  //              localeProvider.getLocale(inputMessage)));
  //      send(sendMessage);
  //      send(buildHelpMessage(inputMessage, command.get(COMMAND),
  // TEMPLATE_SUBSCRIPTION_EXCEPTION));
  //    } catch (BotException e) {
  //      send(buildHelpMessage(inputMessage, command.get(COMMAND),
  // TEMPLATE_SUBSCRIPTION_EXCEPTION));
  //    }
  //  }
}
