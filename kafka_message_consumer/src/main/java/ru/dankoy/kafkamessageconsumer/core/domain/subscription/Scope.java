package ru.dankoy.kafkamessageconsumer.core.domain.subscription;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Scope {

  private long id;

  private String name;

}