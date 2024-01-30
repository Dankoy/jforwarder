package ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.subscription;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.communitysubscription.Chat;

@EqualsAndHashCode
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

  private long id;

  private Chat chat;

  private String lastPermalink;

}
