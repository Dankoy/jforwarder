package ru.dankoy.telegrambot.core.service.localization;

import java.util.Locale;

public interface LocalisationService {

  String getLocalizedMessage(String code, Object[] objects, Locale locale);
}
