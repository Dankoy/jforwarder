package ru.dankoy.telegrambot.core.service.bot;

import lombok.Getter;

@Getter
public enum TelegramBotApiErrorMessages {
  TOPIC_CLOSED("TOPIC_CLOSED"),
  TOPIC_DELETED("TOPIC_DELETED");

  private final String message;

  TelegramBotApiErrorMessages(String message) {
    this.message = message;
  }
}
