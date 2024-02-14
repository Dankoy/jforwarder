package ru.dankoy.telegrambot.core.service.localeprovider;

import java.util.Locale;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * @author turtality
 *     <p>Provides only locale from application settings
 */
public interface LocaleProvider {

    Locale getLocale(Message message);

    Locale getLocale(String locale);
}
