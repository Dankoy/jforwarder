package ru.dankoy.telegrambot.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ToString
@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "application.bot")
public class TelegramBotProperties {

  private final String name;
  private final String token;

}
