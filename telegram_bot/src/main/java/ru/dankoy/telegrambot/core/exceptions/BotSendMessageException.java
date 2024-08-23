package ru.dankoy.telegrambot.core.exceptions;

public class BotSendMessageException extends BotException {

  public BotSendMessageException(String message) {
    super(message);
  }

  public BotSendMessageException(String message, Exception e) {
    super(message, e);
  }
}
