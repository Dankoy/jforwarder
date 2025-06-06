package ru.dankoy.telegrambot.core.exceptions;

import org.telegram.telegrambots.meta.api.objects.message.Message;

import lombok.Getter;

@Getter
public class BotFlowException extends BotException {

  private final Message inputBotMessage;

  private final String localizedSourceMessage;

  private final transient Object[] objects;

  public BotFlowException(
      String message, Message inputBotMessage, String localizedSourceMessage, Object[] objects) {
    super(message);
    this.inputBotMessage = inputBotMessage;
    this.localizedSourceMessage = localizedSourceMessage;
    this.objects = objects;
  }

  public BotFlowException(
      String message,
      Exception e,
      Message inputBotMessage,
      String localizedSourceMessage,
      Object[] objects) {
    super(message, e);
    this.inputBotMessage = inputBotMessage;
    this.localizedSourceMessage = localizedSourceMessage;
    this.objects = objects;
  }
}
