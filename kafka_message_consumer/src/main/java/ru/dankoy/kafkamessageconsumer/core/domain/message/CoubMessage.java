package ru.dankoy.kafkamessageconsumer.core.domain.message;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dankoy.kafkamessageconsumer.core.domain.coub.Coub;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Chat;

@Getter
@SuperBuilder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class CoubMessage {
  private long id;
  private Chat chat;
  private Coub coub;
  private String lastPermalink;
}
