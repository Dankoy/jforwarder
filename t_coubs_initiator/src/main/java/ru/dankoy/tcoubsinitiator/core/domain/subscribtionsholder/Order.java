package ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder;

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
  private String value;
}
