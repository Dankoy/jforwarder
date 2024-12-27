package ru.dankoy.subscriptions_scheduler.core.domain.subscribtionsholder.subscription;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptions_scheduler.core.domain.subscribtionsholder.Chat;

@EqualsAndHashCode
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

  private long id;

  private Chat chat;

  private LocalDateTime createdAt;

  private LocalDateTime modifiedAt;
}
