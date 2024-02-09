package ru.dankoy.telegrambot.core.service.localeprovider;

import java.util.Locale;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class LocaleProviderImpl implements LocaleProvider {

  @Override
  public Locale getLocaleFromMessage(Message message) {
    return Locale.forLanguageTag(message.getFrom().getLanguageCode());
  }
}
