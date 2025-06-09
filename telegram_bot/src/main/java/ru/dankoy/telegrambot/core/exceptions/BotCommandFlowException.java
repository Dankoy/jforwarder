package ru.dankoy.telegrambot.core.exceptions;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Getter
public class BotCommandFlowException extends BotException {

  private final Message inputBotMessage;

  private final String freemarkerTemplateName;

  private final String[] command;

  public BotCommandFlowException(
      String message, Message inputBotMessage, String freemarkerTemplateName, String[] command) {
    super(message);
    this.inputBotMessage = inputBotMessage;
    this.freemarkerTemplateName = freemarkerTemplateName;
    this.command = command;
  }

  public BotCommandFlowException(
      String message,
      Exception e,
      Message inputBotMessage,
      String freemarkerTemplateName,
      String[] command) {
    super(message, e);
    this.inputBotMessage = inputBotMessage;
    this.freemarkerTemplateName = freemarkerTemplateName;
    this.command = command;
  }
}
