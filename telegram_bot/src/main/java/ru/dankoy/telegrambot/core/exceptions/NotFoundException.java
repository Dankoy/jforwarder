package ru.dankoy.telegrambot.core.exceptions;

public class NotFoundException extends BotException {

  public NotFoundException(String message) {
    super(message);
  }

  public NotFoundException(String message, Exception e) {
    super(message, e);
  }
}
