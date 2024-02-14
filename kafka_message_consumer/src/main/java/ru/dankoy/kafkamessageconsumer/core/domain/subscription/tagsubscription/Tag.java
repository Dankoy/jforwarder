package ru.dankoy.kafkamessageconsumer.core.domain.subscription.tagsubscription;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

  private long id;

  private String title;

}
