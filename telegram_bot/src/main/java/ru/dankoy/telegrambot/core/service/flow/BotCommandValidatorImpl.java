package ru.dankoy.telegrambot.core.service.flow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.dankoy.telegrambot.core.service.bot.commands.CommandsExtractorService;

@Component
@RequiredArgsConstructor
@Slf4j
public class BotCommandValidatorImpl implements BotCommandValidator {

  private final CommandsExtractorService commandsExtractorService;

  /**
   * Checks if message is valid for bot commands
   *
   * @param message bot message
   * @return spring integration flow message
   */
  @Override
  public org.springframework.messaging.Message<Message> isValid(Message message) {

    var locale = message.getFrom().getLanguageCode();
    var text = message.getText();

    var optional = commandsExtractorService.getCommand(locale, text);
    var isValid = optional.isPresent();

    if (optional.isEmpty()) {
      log.warn("Command '{}' is not valid. Send message to null channel", text);
    }

    return MessageBuilder.withPayload(message)
        .setHeader("isValid", isValid)
        .setErrorChannelName("errorChannel")
        .build();
  }
}
