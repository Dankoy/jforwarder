package ru.dankoy.coubconnector.coub_connector.core.domain.subscriberholder.subscription;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
 * Shows subscription of chat ids to some community
 */

@EqualsAndHashCode
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

  private long id;
  private long externalId;
  private String name;
  private String lastPermalink;
  private List<String> chatId;
  private Section section;

}
