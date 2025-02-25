package ru.dankoy.telegramchatservice.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatCreateDTO {

  private long chatId;

  private String type;

  private String title;

  private String firstName;

  private String lastName;

  private String username;

  private boolean active;

  private Integer messageThreadId;

}
