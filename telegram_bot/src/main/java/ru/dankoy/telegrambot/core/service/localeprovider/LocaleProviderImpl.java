package ru.dankoy.telegrambot.core.service.localeprovider;

import java.util.Locale;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.dankoy.telegrambot.config.bot.properties.LocaleConfig;

@Service
@RequiredArgsConstructor
public class LocaleProviderImpl implements LocaleProvider {

  private final LocaleConfig localeConfig;

  @Override
  public Locale getLocale(Message message) {

    var langCode = message.getFrom().getLanguageCode();

    return checkAndReturn(langCode);
  }

  @Override
  public Locale getLocale(String locale) {

    return checkAndReturn(locale);
  }

  private Locale checkAndReturn(String locale) {
    if (Objects.isNull(locale)) {
      return localeConfig.getDefaultLocale();
    } else {
      return Locale.of(locale);
    }
  }
}
