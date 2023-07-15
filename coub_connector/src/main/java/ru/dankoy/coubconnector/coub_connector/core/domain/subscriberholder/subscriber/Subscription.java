package ru.dankoy.coubconnector.coub_connector.core.domain.subscriberholder.subscriber;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
 * Shows subscription of chat ids to some community
 */

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

  private String communityId;
  private String communityName;
  private List<String> chatId;

}
