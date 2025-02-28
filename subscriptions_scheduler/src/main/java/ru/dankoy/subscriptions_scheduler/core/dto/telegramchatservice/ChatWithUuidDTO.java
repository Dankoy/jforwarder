package ru.dankoy.subscriptions_scheduler.core.dto.telegramchatservice;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChatWithUuidDTO {

  @NotNull private UUID id;

  @NotNull(message = "Chat ID must not be null")
  private long chatId;

  private String type;

  private String title;

  private String firstName;

  private String lastName;

  private String username;

  @NotNull(message = "Active status must not be null")
  private boolean active;

  private Integer messageThreadId;

  @NotNull(message = "Date created must not be null")
  private LocalDateTime dateCreated;

  private LocalDateTime dateModified;
}
