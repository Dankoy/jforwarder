package ru.dankoy.telegrambot.core.domain.channel;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Channel {

  private long id;

  private String title;

  public Channel(String title) {
    this.title = title;
  }
}
