package ru.dankoy.subscriptions_scheduler.core.domain.subscribtionsholder.communitysubscription;

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
public class Section {

  private long id;
  private String name;
}
