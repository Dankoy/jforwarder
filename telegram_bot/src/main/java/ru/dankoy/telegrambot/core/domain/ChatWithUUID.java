package ru.dankoy.telegrambot.core.domain;

import java.util.UUID;
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
public class ChatWithUUID {

  private UUID id;
  private long chatId;
  private String type;
  private String title;
  private String firstName;
  private String lastName;
  private String username;

  @Setter private boolean active;

  private Integer messageThreadId;

  public ChatWithUUID(long chatId, Integer messageThreadId) {
    this.chatId = chatId;
    this.messageThreadId = messageThreadId;
  }
}
