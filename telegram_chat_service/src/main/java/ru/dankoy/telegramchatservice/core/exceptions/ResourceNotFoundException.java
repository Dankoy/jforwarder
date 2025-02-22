package ru.dankoy.telegramchatservice.core.exceptions;

import java.io.Serial;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends SubscriptionHolderException {

  @Serial private static final long serialVersionUID = 1L;

  public ResourceNotFoundException(String message) {
    super(message);
  }

  public ResourceNotFoundException(String message, Exception ex) {
    super(message, ex);
  }
}
