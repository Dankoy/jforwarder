package ru.dankoy.telegramchatservice.core.domain.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegexSearchCriteria {
  private String key;
  private String operation;
  private Object value;
}
