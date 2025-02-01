package ru.dankoy.tcoubsinitiator.core.domain.subscribtionsholder.registry;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SentCoubsRegistry {

  private long id;

  private String coubPermalink;

  private LocalDateTime dateTime;
}
