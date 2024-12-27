package ru.dankoy.subscriptions_scheduler.core.domain.subscribtionsholder;

import java.time.LocalDateTime;
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
public class Chat {

  private long id;
  private long chatId;
  private Integer messageThreadId;
  private LocalDateTime dateCreated;
  private LocalDateTime dateModified;
}
