package ru.dankoy.kafkamessageconsumer.core.domain.subscription.channelsubscription;

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
public class Channel {

  private long id;

  private String title;

  private String permalink;
}
