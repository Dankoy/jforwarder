package ru.dankoy.telegrambot.config.bot.properties;

import java.util.Locale;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ToString
@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "application.bot")
public class TelegramBotProperties implements LocaleConfig, BotCredentialsConfig, BotNameProvider {

  private final String name;
  private final String token;

  private final Locale[] locales;
  private final Locale defaultLocale;
}
