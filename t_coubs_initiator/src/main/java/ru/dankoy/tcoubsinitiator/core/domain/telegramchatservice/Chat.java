package ru.dankoy.tcoubsinitiator.core.domain.telegramchatservice;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Chat {

  private UUID id;
  private long chatId;
  private Integer messageThreadId;
  private String username;
}
