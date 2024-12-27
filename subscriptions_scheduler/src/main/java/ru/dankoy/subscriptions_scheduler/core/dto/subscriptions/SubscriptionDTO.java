package ru.dankoy.subscriptions_scheduler.core.dto.subscriptions;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SubscriptionDTO {

  @Min(value = 1, message = "Subscription ID must be greater than 0")
  private long id;

  @NotNull(message = "Chat must not be null")
  private ChatDTO chat;

  @NotNull(message = "CreatedAt must not be null")
  private LocalDateTime createdAt;

  @NotNull(message = "ModifiedAt must not be null")
  private LocalDateTime modifiedAt;
}
