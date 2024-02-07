package ru.dankoy.kafkamessageproducer.core.domain.message;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dankoy.kafkamessageproducer.core.domain.coub.Coub;
import ru.dankoy.kafkamessageproducer.core.domain.subscription.Chat;

@Getter
@SuperBuilder
@ToString
public class CoubMessage {
  private long id;
  private Chat chat;
  private Coub coub;
  private String lastPermalink;
}
