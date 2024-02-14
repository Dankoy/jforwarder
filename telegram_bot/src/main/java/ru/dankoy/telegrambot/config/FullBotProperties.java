package ru.dankoy.telegrambot.config;

import java.util.Locale;

public interface FullBotProperties {

    String getName();

    String getToken();

    Locale getDefaultLocale();

    Locale[] getLocales();
}
