package ru.dankoy.telegramchatservice.core.exceptions;

public class SubscriptionHolderException extends RuntimeException {

  public SubscriptionHolderException(String message, Exception ex) {

    super(message, ex);
  }

  public SubscriptionHolderException(String message) {

    super(message);
  }
}
