package ru.dankoy.telegrambot.core.domain.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dankoy.telegrambot.core.domain.coub.Coub;
import ru.dankoy.telegrambot.core.domain.Chat;

@Getter
@SuperBuilder
@ToString
@NoArgsConstructor
public class CoubMessage {
  private long id;
  private Chat chat;
  private Coub coub;
  private String lastPermalink;
}
