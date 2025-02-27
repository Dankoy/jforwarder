package ru.dankoy.telegramchatservice.core.domain.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {

  private UUID id;
  private Long chatId;
  private String type;
  private String title;
  private String firstName;
  private String lastName;
  private String username;
  private Boolean active;
  private Integer messageThreadId;
  private LocalDateTime dateCreated;
  private LocalDateTime dateModified;
}
