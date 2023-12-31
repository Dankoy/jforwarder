package ru.dankoy.telegrambot.core.domain.tagsubscription;


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
public class Order {

  private long id;

  private String name;

  public Order(String name) {
    this.name = name;
  }
}
