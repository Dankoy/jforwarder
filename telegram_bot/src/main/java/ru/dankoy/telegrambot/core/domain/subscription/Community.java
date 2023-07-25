package ru.dankoy.telegrambot.core.domain.subscription;


import java.util.Set;
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
public class Community {

  private long id;
  private long externalId;
  private String name;

  private Set<Section> sections;

  public Community(String name) {
    this.name = name;
  }

}
