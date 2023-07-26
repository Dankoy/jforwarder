package ru.dankoy.kafkamessageconsumer.core.domain.subscription;


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
public class Community {

  private long id;
  private long externalId;
  private String name;

}
