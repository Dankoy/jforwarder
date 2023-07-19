package ru.dankoy.coubconnector.coub_connector.core.domain.subscribtionsholder.subscription;


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

  private long id;
  private long chatId;
  private String username;

}
