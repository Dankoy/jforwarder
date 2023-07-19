package ru.dankoy.coubconnector.coub_connector.core.domain.subscribtionsholder.subscription;


import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.dankoy.coubconnector.coub_connector.core.domain.coubcom.coub.Coub;

@EqualsAndHashCode
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

  private long id;
  private Community community;
  private Chat chat;
  private Section section;
  private String lastPermalink;


  @Setter
  private List<Coub> coubs = new ArrayList<>();

  public void addCoubs(List<Coub> coubsToAdd) {

    for (var coub : coubsToAdd) {

      var newCoub = new Coub(
          coub.getId(),
          coub.getTitle(),
          coub.getPermalink(),
          coub.getUrl(),
          coub.getPublishedAt()
      );
      coubs.add(newCoub);

    }


  }

}
