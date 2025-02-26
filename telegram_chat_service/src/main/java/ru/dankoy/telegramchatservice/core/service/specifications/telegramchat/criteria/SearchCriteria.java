package ru.dankoy.telegramchatservice.core.service.specifications.telegramchat.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
  private String key;
  private String operation;
  private Object value;
}
