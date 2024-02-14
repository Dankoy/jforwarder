package ru.dankoy.telegrambot.config;

import java.util.Locale;

public interface LocaleConfig extends FullBotProperties {

    Locale getDefaultLocale();

    Locale[] getLocales();
}
