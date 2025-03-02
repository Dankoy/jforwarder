package ru.dankoy.telegramchatservice.core.domain.search;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoSearchCriteria {

  private UUID id;
  private Long chatId;

  private String type;

  private String title;

  private String firstName;

  private String lastName;

  private String username;

  private Boolean active;

  private Integer messageThreadId;
}
