package ru.dankoy.kafkamessageproducer.core.domain.registry;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Subscription;

@EqualsAndHashCode
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SentCoubsRegistry {

  private long id;

  private Subscription subscription;

  private String coubPermalink;

  private LocalDateTime dateTime;
}
