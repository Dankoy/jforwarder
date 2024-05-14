package ru.dankoy.telegrambot.core.service.flow;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.dankoy.telegrambot.core.domain.subscription.SubscriptionType;
import ru.dankoy.telegrambot.core.exceptions.BotCommandFlowException;
import ru.dankoy.telegrambot.core.exceptions.BotFlowException;

@Component
@Slf4j
public class CommandParserServiceImpl implements CommandParserService {

  @Override
  public org.springframework.messaging.Message<Message> parseSubscribeCommand(Message message) {
    Map<String, String> result = new HashMap<>();

    String[] command = message.getText().split(" ");

    if (command.length < 4) {
      log.error("Expected valid command but got - {}", Arrays.asList(command));

      throw new BotCommandFlowException(
          "Invalid command", message, "subscription_exception.ftl", command);
    }

    checkSubscriptionTypeInCommand(message, command);

    // Get all words after 0 and last element and concat in one string
    var s = Arrays.stream(command, 2, command.length - 1).collect(Collectors.joining(" "));

    result.put(CommandConstraints.COMMAND.getConstraint(), command[0]);
    result.put(CommandConstraints.COMMAND_SUBSCRIPTION_TYPE_FIELD.getConstraint(), command[1]);
    result.put(CommandConstraints.COMMAND_FIRST_FIELD.getConstraint(), s);
    result.put(
        CommandConstraints.COMMAND_SECOND_FIELD.getConstraint(), command[command.length - 1]);

    return MessageBuilder.withPayload(message)
        .setHeader("parsedCommand", result)
        .setErrorChannelName("errorChannel")
        .build();
  }

  private void checkSubscriptionTypeInCommand(Message inputMessage, String[] command) {
    try {
      SubscriptionType.valueOf(command[1].toUpperCase());
    } catch (IllegalArgumentException e) {

      var m =
          String.format(
              "Expected one of %s, but got %s",
              Arrays.asList(SubscriptionType.values()), command[1]);

      log.error(m);

      throw new BotFlowException(
          m,
          inputMessage,
          "illegalBotCommand",
          new Object[] {Arrays.asList(SubscriptionType.values()).toString().toLowerCase()});
    }
  }
}
