package ru.dankoy.telegrambot.core.service.flow;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.dankoy.telegrambot.core.service.bot.commands.CommandsExtractorService;
import ru.dankoy.telegrambot.core.service.bot.commands.HelpCommand;

@RequiredArgsConstructor
@Component
public class BotMessageRouterImpl implements BotMessageRouter {

  private static final String COMMAND_HEADER = "command";

  private final CommandsExtractorService commandsExtractorService;

  @Override
  public org.springframework.messaging.Message<Message> commandRoute(Message inputMessage) {

    var messageText = inputMessage.getText();

    var message = MessageBuilder.withPayload(inputMessage);

    var optionalClass =
        commandsExtractorService.getCommand(inputMessage.getFrom().getLanguageCode(), messageText);

    optionalClass.ifPresentOrElse(
        c -> message.setHeader(COMMAND_HEADER, c.getClass().getSimpleName()),
        () -> message.setHeader(COMMAND_HEADER, HelpCommand.class.getSimpleName()));

    return message.build();
  }
}
