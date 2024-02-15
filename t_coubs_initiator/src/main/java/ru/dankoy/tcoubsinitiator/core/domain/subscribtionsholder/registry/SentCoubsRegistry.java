package ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.registry;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.subscription.Subscription;

@EqualsAndHashCode
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SentCoubsRegistry {

  private long id;

  private Subscription subscription;

  private String coubPermalink;

  private LocalDateTime dateTime;
}
