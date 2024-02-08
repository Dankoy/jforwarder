package ru.dankoy.kafkamessageconsumer.core.domain.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dankoy.kafkamessageconsumer.core.domain.communitysubscription.Chat;
import ru.dankoy.kafkamessageconsumer.core.domain.coub.Coub;

@Getter
@SuperBuilder
@ToString
@NoArgsConstructor
public class CoubMessage {
  private long id;
  private Chat chat;
  private Coub coub;
  private String lastPermalink;
}
