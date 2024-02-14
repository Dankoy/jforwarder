package ru.dankoy.telegrambot.core.domain.subscription.channel;

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
  private String permalink;

  public Channel(String permalink) {
    this.permalink = permalink;
  }
}
