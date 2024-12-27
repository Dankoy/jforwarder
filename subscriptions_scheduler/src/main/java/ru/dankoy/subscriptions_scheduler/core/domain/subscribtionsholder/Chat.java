package ru.dankoy.subscriptions_scheduler.core.domain.subscribtionsholder;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(exclude = {"dateCreated", "dateModified"})
@AllArgsConstructor
public class Chat {

  private long id;
  private long chatId;
  private Integer messageThreadId;
  private LocalDateTime dateCreated;
  private LocalDateTime dateModified;
}
