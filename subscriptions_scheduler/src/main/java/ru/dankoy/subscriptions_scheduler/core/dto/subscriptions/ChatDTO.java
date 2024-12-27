package ru.dankoy.subscriptions_scheduler.core.dto.subscriptions;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChatDTO {

  @Min(value = 1, message = "ID must be greater than 0")
  private long id;

  @NotNull(message = "Chat ID must not be null")
  private long chatId;

  @NotNull(message = "Active status must not be null")
  private boolean active;

  private Integer messageThreadId;

  @NotNull(message = "Date created must not be null")
  private LocalDateTime dateCreated;

  private LocalDateTime dateModified;
}
