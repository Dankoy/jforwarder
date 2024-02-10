package ru.dankoy.telegrambot.core.service.localization;

import java.util.Locale;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.dankoy.telegrambot.config.LocaleConfig;

@Service
@RequiredArgsConstructor
public class LocalisationServiceImpl implements LocalisationService {

  private final LocaleConfig localeConfig;

  private final MessageSource messageSource;

  @Override
  public String getLocalizedMessage(String code, Object[] objects, Locale locale) {
    Objects.requireNonNull(code);
    if (locale == null) {
      locale = localeConfig.getDefaultLocale();
    }

    return messageSource.getMessage(code, objects, locale);
  }
}
