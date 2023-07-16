package ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Object> handleResourceNotFoundException(
      ResourceNotFoundException ex) {

    List<String> details = new ArrayList<>();
    details.add(ex.getMessage());
    details.add(Arrays.toString(ex.getStackTrace()));

    ApiError err = new ApiError(
        LocalDateTime.now(),
        HttpStatus.BAD_REQUEST,
        "Resource Not Found",
        details);

    return ResponseEntityBuilder.build(err);
  }


}
