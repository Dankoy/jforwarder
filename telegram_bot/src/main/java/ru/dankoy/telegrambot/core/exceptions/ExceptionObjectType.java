package ru.dankoy.telegrambot.core.exceptions;

import lombok.Getter;

@Getter
public enum ExceptionObjectType {
  TAG("tagException"),
  COMMUNITY("communityException"),
  CHANNEL("channelException"),
  SECTION("sectionException"),
  ORDER("orderException");

  private final String type;

  ExceptionObjectType(String type) {
    this.type = type;
  }
}
