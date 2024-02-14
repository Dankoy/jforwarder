package ru.dankoy.telegrambot.core.service.localeprovider;

import java.util.Locale;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class LocaleProviderImpl implements LocaleProvider {

  @Override
  public Locale getLocale(Message message) {
    return Locale.forLanguageTag(message.getFrom().getLanguageCode());
  }

  @Override
  public Locale getLocale(String locale) {
    return Locale.forLanguageTag(locale);
  }
}
