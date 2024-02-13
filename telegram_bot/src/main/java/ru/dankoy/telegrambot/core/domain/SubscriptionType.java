package ru.dankoy.telegrambot.core.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubscriptionType {
  COMMUNITY("community"),
  TAG("tag"),
  CHANNEL("channel");

  private final String type;

}
