package ru.dankoy.telegrambot.core.domain.subscription;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SubscriptionType {
  COMMUNITY("community"),
  TAG("tag"),
  CHANNEL("channel");

  private final String type;

}
