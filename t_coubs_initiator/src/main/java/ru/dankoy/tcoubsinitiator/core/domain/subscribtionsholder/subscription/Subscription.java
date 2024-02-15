package ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.subscription;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.Coub;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.Chat;

@EqualsAndHashCode
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

  private long id;

  private Chat chat;

  private String lastPermalink;

  @Setter private List<Coub> coubs = new ArrayList<>();

  public void addCoubs(List<Coub> coubsToAdd) {

    for (var coub : coubsToAdd) {

      var newCoub =
          new Coub(
              coub.getId(),
              coub.getTitle(),
              coub.getPermalink(),
              coub.getUrl(),
              coub.getPublishedAt());
      coubs.add(newCoub);
    }
  }
}
