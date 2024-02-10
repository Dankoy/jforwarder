package ru.dankoy.telegrambot.core.exceptions;

import lombok.Getter;

@Getter
public class NotFoundException extends BotException {

  private final ExceptionObjectType exceptionObjectType;
  private final String value;

  public NotFoundException(ExceptionObjectType exceptionObjectType, String value, String message) {
    super(message);
    this.exceptionObjectType = exceptionObjectType;
    this.value = value;
  }

  public NotFoundException(
      ExceptionObjectType exceptionObjectType, String value, String message, Exception e) {
    super(message, e);
    this.exceptionObjectType = exceptionObjectType;
    this.value = value;
  }
}
