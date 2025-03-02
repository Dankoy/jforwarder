package ru.dankoy.telegramchatservice.core.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceConflictException extends SubscriptionHolderException {

  @Serial private static final long serialVersionUID = 1L;

  public ResourceConflictException(String message) {
    super(message);
  }
}
