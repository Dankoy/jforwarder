package ru.dankoy.telegrambot.core.domain.subscription;


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
public class Section {

  private long id;
  private String name;

  public Section(String name) {
    this.name = name;
  }

}
