package ru.dankoy.kafkamessageconsumer.core.domain.tagsubscription;


import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.dankoy.kafkamessageconsumer.core.domain.communitysubscription.Chat;
import ru.dankoy.kafkamessageconsumer.core.domain.coub.Coub;


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
