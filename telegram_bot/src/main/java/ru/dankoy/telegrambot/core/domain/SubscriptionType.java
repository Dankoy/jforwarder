package ru.dankoy.telegrambot.core.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubscriptionType {
  COMMUNITY("community"),
  TAG("tag");

  private final String type;

}
