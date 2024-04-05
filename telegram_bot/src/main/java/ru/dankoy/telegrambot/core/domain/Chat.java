package ru.dankoy.telegrambot.core.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Chat {

  private long id;
  private long chatId;
  private String type;
  private String title;
  private String firstName;
  private String lastName;
  private String username;

  @Setter private boolean active;

  private Integer messageThreadId;

  public Chat(long chatId, Integer messageThreadId) {
    this.chatId = chatId;
    this.messageThreadId = messageThreadId;
  }
}
