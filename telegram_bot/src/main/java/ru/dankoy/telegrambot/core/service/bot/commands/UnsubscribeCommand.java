package ru.dankoy.telegrambot.core.service.bot.commands;

import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.dankoy.telegrambot.core.domain.subscription.SubscriptionType;
import ru.dankoy.telegrambot.core.dto.flow.CreateReplyUnsubscribeDto;
import ru.dankoy.telegrambot.core.exceptions.BotCommandFlowException;
import ru.dankoy.telegrambot.core.exceptions.BotException;
import ru.dankoy.telegrambot.core.service.flow.CommandConstraints;
import ru.dankoy.telegrambot.core.service.subscription.ChannelSubscriptionService;
import ru.dankoy.telegrambot.core.service.subscription.CommunitySubscriptionService;
import ru.dankoy.telegrambot.core.service.subscription.TagSubscriptionService;

@EqualsAndHashCode(callSuper = true)
@Slf4j
public class UnsubscribeCommand extends BotCommand {

  private static final String TEMPLATE_SUBSCRIPTION_EXCEPTION = "subscription_exception.ftl";

  @Qualifier("communitySubscriptionServiceHttpClient")
  private final transient CommunitySubscriptionService communitySubscriptionService;

  @Qualifier("tagSubscriptionServiceHttpClient")
  private final transient TagSubscriptionService tagSubscriptionService;

  @Qualifier("channelSubscriptionServiceHttpClient")
  private final transient ChannelSubscriptionService channelSubscriptionService;

  public UnsubscribeCommand(
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

  public CreateReplyUnsubscribeDto unsubscribe(
      org.springframework.messaging.Message<Message> message) {

    // find route by first word after command

    var inputMessage = message.getPayload();

    Map<String, String> command = (Map<String, String>) message.getHeaders().get("parsedCommand");
    assert command != null;

    // route
    if (command
        .get(CommandConstraints.COMMAND_SUBSCRIPTION_TYPE_FIELD.getConstraint())
        .equals(SubscriptionType.COMMUNITY.getType())) {

      return unsubscribeFromCommunity(command, inputMessage);

    } else if (command
        .get(CommandConstraints.COMMAND_SUBSCRIPTION_TYPE_FIELD.getConstraint())
        .equals(SubscriptionType.TAG.getType())) {

      return unsubscribeFromTag(command, inputMessage);

    } else if (command
        .get(CommandConstraints.COMMAND_SUBSCRIPTION_TYPE_FIELD.getConstraint())
        .equals(SubscriptionType.CHANNEL.getType())) {

      return unsubscribeFromChannel(command, inputMessage);
    }

    return null;
  }

  private CreateReplyUnsubscribeDto unsubscribeFromCommunity(
      Map<String, String> command, Message inputMessage) {

    var communityName = command.get(CommandConstraints.COMMAND_FIRST_FIELD.getConstraint());
    var sectionName = command.get(CommandConstraints.COMMAND_SECOND_FIELD.getConstraint());

    try {
      communitySubscriptionService.unsubscribe(
          communityName,
          sectionName,
          inputMessage.getChat().getId(),
          inputMessage.getMessageThreadId());

      return new CreateReplyUnsubscribeDto(inputMessage, new Object[] {communityName, sectionName});

    } catch (BotException e) {
      throw new BotCommandFlowException(
          e.getMessage(),
          inputMessage,
          TEMPLATE_SUBSCRIPTION_EXCEPTION,
          new String[] {command.get(CommandConstraints.COMMAND.getConstraint())});
    }
  }

  private CreateReplyUnsubscribeDto unsubscribeFromTag(
      Map<String, String> command, Message inputMessage) {

    var tagName = command.get(CommandConstraints.COMMAND_FIRST_FIELD.getConstraint());
    var orderValue = command.get(CommandConstraints.COMMAND_SECOND_FIELD.getConstraint());

    try {

      tagSubscriptionService.unsubscribe(
          tagName,
          orderValue,
          "all",
          "",
          inputMessage.getChat().getId(),
          inputMessage.getMessageThreadId());

      return new CreateReplyUnsubscribeDto(inputMessage, new Object[] {tagName, orderValue});

    } catch (BotException e) {

      throw new BotCommandFlowException(
          e.getMessage(),
          inputMessage,
          TEMPLATE_SUBSCRIPTION_EXCEPTION,
          new String[] {command.get(CommandConstraints.COMMAND.getConstraint())});
    }
  }

  private CreateReplyUnsubscribeDto unsubscribeFromChannel(
      Map<String, String> command, Message inputMessage) {

    var channelPermalink = command.get(CommandConstraints.COMMAND_FIRST_FIELD.getConstraint());
    var orderValue = command.get(CommandConstraints.COMMAND_SECOND_FIELD.getConstraint());

    try {

      channelSubscriptionService.unsubscribe(
          channelPermalink,
          orderValue,
          "all",
          "",
          inputMessage.getChat().getId(),
          inputMessage.getMessageThreadId());

      return new CreateReplyUnsubscribeDto(
          inputMessage, new Object[] {channelPermalink, orderValue});

    } catch (BotException e) {
      throw new BotCommandFlowException(
          e.getMessage(),
          inputMessage,
          TEMPLATE_SUBSCRIPTION_EXCEPTION,
          new String[] {command.get(CommandConstraints.COMMAND.getConstraint())});
    }
  }
}
