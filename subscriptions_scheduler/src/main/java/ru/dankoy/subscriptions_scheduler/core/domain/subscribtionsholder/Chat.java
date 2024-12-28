package ru.dankoy.subscriptions_scheduler.core.domain.subscribtionsholder;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.dankoy.subscriptions_scheduler.core.domain.subscribtionsholder.subscription.Subscription;

@Getter
@ToString
@EqualsAndHashCode(exclude = {"dateCreated", "dateModified", "subscriptions"})
@AllArgsConstructor
public class Chat {

  private long id;
  private long chatId;
  private boolean active;
  private Integer messageThreadId;
  private LocalDateTime dateCreated;
  private LocalDateTime dateModified;
  private List<Subscription> subscriptions;
}
