package ru.dankoy.kafkamessageconsumer.core.domain.subscription;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dankoy.kafkamessageconsumer.core.domain.coub.Coub;

@EqualsAndHashCode
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Subscription {

  private long id;

  private Chat chat;

  private String lastPermalink;

  private final List<Coub> coubs = new ArrayList<>();
}
