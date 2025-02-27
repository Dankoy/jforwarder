package ru.dankoy.telegramchatservice.core.domain.search;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DtoSearchCriteria {

  private Long chatId;

  private String type;

  private String title;

  private String firstName;

  private String lastName;

  private String username;

  private Boolean active;

  private Long messageThreadId;
}
