package ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.tagsubscription;


import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.Coub;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.communitysubscription.Chat;

@Getter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagSubscription {


  private long id;

  private Tag tag;

  private Chat chat;

  private Order order;

  private Scope scope;

  private Type type;

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
