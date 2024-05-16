package ru.dankoy.telegrambot.config.bot.properties;

import java.util.Locale;

public interface LocaleConfig extends FullBotProperties {

  Locale getDefaultLocale();

  Locale[] getLocales();
}
