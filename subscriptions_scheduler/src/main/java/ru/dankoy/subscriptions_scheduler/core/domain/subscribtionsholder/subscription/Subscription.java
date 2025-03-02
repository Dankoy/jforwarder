package ru.dankoy.subscriptions_scheduler.core.domain.subscribtionsholder.subscription;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode(exclude = {"createdAt", "modifiedAt"})
@Getter
@ToString
@AllArgsConstructor
public class Subscription {

  private long id;

  private String lastPermalink;

  private LocalDateTime createdAt;

  private LocalDateTime modifiedAt;

  private UUID chatUuid;
}
