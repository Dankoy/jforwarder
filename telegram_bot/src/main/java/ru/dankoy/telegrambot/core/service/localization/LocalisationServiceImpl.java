package ru.dankoy.telegrambot.core.service.localization;

import java.util.Locale;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalisationServiceImpl implements LocalisationService {

  private final MessageSource messageSource;

  @Override
  public String getLocalizedMessage(String code, Object[] objects, Locale locale) {
    Objects.requireNonNull(code);
    Objects.requireNonNull(objects);
    Objects.requireNonNull(locale);
    return messageSource.getMessage(code, objects, locale);
  }
}
