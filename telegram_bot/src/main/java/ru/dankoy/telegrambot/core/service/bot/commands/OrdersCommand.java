package ru.dankoy.telegrambot.core.service.bot.commands;

import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.dankoy.telegrambot.core.domain.subscription.Order;
import ru.dankoy.telegrambot.core.domain.subscription.SubscriptionType;
import ru.dankoy.telegrambot.core.dto.flow.CreateReplyOrdersDto;
import ru.dankoy.telegrambot.core.service.flow.CommandConstraints;
import ru.dankoy.telegrambot.core.service.order.OrderService;

@EqualsAndHashCode(callSuper = true)
@Slf4j
public class OrdersCommand extends BotCommand {

  private final transient OrderService orderService;

  public OrdersCommand(String command, String description, OrderService orderService) {
    super(command, description);
    this.orderService = orderService;
  }

  public CreateReplyOrdersDto orders(org.springframework.messaging.Message<Message> message) {

    var inputMessage = message.getPayload();

    Map<String, SubscriptionType> command =
        (Map<String, SubscriptionType>) message.getHeaders().get("parsedCommand");

    assert command != null;

    List<Order> orders =
        orderService.findAllByType(
            command.get(CommandConstraints.COMMAND_SUBSCRIPTION_TYPE_FIELD.getConstraint()));

    // escape special chars in order names
    var updated =
        orders.stream().map(order -> new Order(escapeMetaCharacters(order.getValue()))).toList();

    return new CreateReplyOrdersDto(inputMessage, updated);
  }

  private String escapeMetaCharacters(String inputString) {
    final String[] metaCharacters = {
      "\\", "^", "$", "{", "}", "[", "]", "(", ")", ".", "*", "+", "?", "|", "<", ">", "-", "&",
      "%", "_"
    };

    for (String metaCharacter : metaCharacters) {
      if (inputString.contains(metaCharacter)) {
        inputString = inputString.replace(metaCharacter, "\\" + metaCharacter);
      }
    }
    return inputString;
  }
}
