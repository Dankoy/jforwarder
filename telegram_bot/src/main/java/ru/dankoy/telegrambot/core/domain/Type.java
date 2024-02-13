package ru.dankoy.telegrambot.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Type {

  private long id;

  private String name;


  public Type(String name) {
    this.name = name;
  }
}
