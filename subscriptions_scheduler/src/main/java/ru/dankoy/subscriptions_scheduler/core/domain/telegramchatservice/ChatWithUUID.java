package ru.dankoy.subscriptions_scheduler.core.domain.telegramchatservice;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(exclude = {"dateCreated", "dateModified"})
@AllArgsConstructor
public class ChatWithUUID {

  private UUID id;
  private long chatId;

  private String type;

  private String title;

  private String firstName;

  private String lastName;

  private String username;

  private boolean active;
  private Integer messageThreadId;
  private LocalDateTime dateCreated;
  private LocalDateTime dateModified;
}
