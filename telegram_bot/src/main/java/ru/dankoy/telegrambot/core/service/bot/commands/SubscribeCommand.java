package ru.dankoy.telegrambot.core.service.bot.commands;

import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.HttpClientErrorException.Conflict;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.dankoy.telegrambot.core.domain.subscription.SubscriptionType;
import ru.dankoy.telegrambot.core.dto.flow.CreateReplySubscribeDto;
import ru.dankoy.telegrambot.core.dto.flow.SubscriptionDto;
import ru.dankoy.telegrambot.core.exceptions.BotCommandFlowException;
import ru.dankoy.telegrambot.core.exceptions.BotException;
import ru.dankoy.telegrambot.core.exceptions.BotFlowException;
import ru.dankoy.telegrambot.core.exceptions.NotFoundException;
import ru.dankoy.telegrambot.core.service.flow.CommandConstraints;
import ru.dankoy.telegrambot.core.service.subscription.ChannelSubscriptionService;
import ru.dankoy.telegrambot.core.service.subscription.CommunitySubscriptionService;
import ru.dankoy.telegrambot.core.service.subscription.TagSubscriptionService;

@EqualsAndHashCode(callSuper = true)
@Slf4j
public class SubscribeCommand extends BotCommand {

  private static final String TEMPLATE_SUBSCRIPTION_EXISTS = "alreadySubscribed";
  private static final String TEMPLATE_SUBSCRIPTION_EXCEPTION = "subscription_exception.ftl";

  @Qualifier("communitySubscriptionServiceHttpClient")
  private final transient CommunitySubscriptionService communitySubscriptionService;

  @Qualifier("tagSubscriptionServiceHttpClient")
  private final transient TagSubscriptionService tagSubscriptionService;

  @Qualifier("channelSubscriptionServiceHttpClient")
  private final transient ChannelSubscriptionService channelSubscriptionService;

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

  public CreateReplySubscribeDto subscribe(org.springframework.messaging.Message<Message> message) {

    // find route by first word after command

    var inputMessage = message.getPayload();

    Map<String, String> command = (Map<String, String>) message.getHeaders().get("parsedCommand");

    // route
    assert command != null;

    if (command
        .get(CommandConstraints.COMMAND_SUBSCRIPTION_TYPE_FIELD.getConstraint())
        .equals(SubscriptionType.TAG.getType())) {

      return subscribeByTag(command, inputMessage);

    } else if (command
        .get(CommandConstraints.COMMAND_SUBSCRIPTION_TYPE_FIELD.getConstraint())
        .equals(SubscriptionType.COMMUNITY.getType())) {

      return subscribeToCommunity(command, inputMessage);

    } else if (command
        .get(CommandConstraints.COMMAND_SUBSCRIPTION_TYPE_FIELD.getConstraint())
        .equals(SubscriptionType.CHANNEL.getType())) {

      return subscribeByChannel(command, inputMessage);
    }

    // todo: look for null fix
    return null;
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

      return new CreateReplySubscribeDto(inputMessage, new SubscriptionDto(s));

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

    } catch (BotException e) {

      throw new BotCommandFlowException(
          e.getMessage(),
          inputMessage,
          TEMPLATE_SUBSCRIPTION_EXCEPTION,
          new String[] {command.get(CommandConstraints.COMMAND.getConstraint())});
    }
  }

  private CreateReplySubscribeDto subscribeByTag(
      Map<String, String> command, Message inputMessage) {

    var tagName = command.get(CommandConstraints.COMMAND_FIRST_FIELD.getConstraint());
    var orderValue = command.get(CommandConstraints.COMMAND_SECOND_FIELD.getConstraint());

    try {

      var s =
          tagSubscriptionService.subscribe(
              tagName,
              orderValue,
              "all",
              "",
              inputMessage.getChat().getId(),
              inputMessage.getMessageThreadId());

      return new CreateReplySubscribeDto(inputMessage, new SubscriptionDto(s));

    } catch (Conflict e) {

      throw new BotFlowException(
          e.getMessage(),
          inputMessage,
          TEMPLATE_SUBSCRIPTION_EXISTS,
          new Object[] {tagName, orderValue});

    } catch (NotFoundException e) {

      throw new BotFlowException(
          e.getMessage(),
          e,
          inputMessage,
          e.getExceptionObjectType().getType(),
          new Object[] {e.getValue()});

    } catch (BotException e) {
      throw new BotCommandFlowException(
          e.getMessage(),
          inputMessage,
          TEMPLATE_SUBSCRIPTION_EXCEPTION,
          new String[] {command.get(CommandConstraints.COMMAND.getConstraint())});
    }
  }

  //
  private CreateReplySubscribeDto subscribeByChannel(
      Map<String, String> command, Message inputMessage) {

    var channelPermalink = command.get(CommandConstraints.COMMAND_FIRST_FIELD.getConstraint());
    var orderValue = command.get(CommandConstraints.COMMAND_SECOND_FIELD.getConstraint());

    try {

      var s =
          channelSubscriptionService.subscribe(
              channelPermalink,
              orderValue,
              "all",
              "",
              inputMessage.getChat().getId(),
              inputMessage.getMessageThreadId());

      return new CreateReplySubscribeDto(inputMessage, new SubscriptionDto(s));

    } catch (Conflict e) {

      throw new BotFlowException(
          e.getMessage(),
          inputMessage,
          TEMPLATE_SUBSCRIPTION_EXISTS,
          new Object[] {channelPermalink, orderValue});

    } catch (NotFoundException e) {

      throw new BotFlowException(
          e.getMessage(),
          e,
          inputMessage,
          e.getExceptionObjectType().getType(),
          new Object[] {e.getValue()});

    } catch (BotException e) {

      throw new BotCommandFlowException(
          e.getMessage(),
          inputMessage,
          TEMPLATE_SUBSCRIPTION_EXCEPTION,
          new String[] {command.get(CommandConstraints.COMMAND.getConstraint())});
    }
  }
}
