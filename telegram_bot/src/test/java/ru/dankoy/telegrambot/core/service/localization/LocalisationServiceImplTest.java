package ru.dankoy.telegrambot.core.service.localization;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Locale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import ru.dankoy.telegrambot.config.LocaleConfig;
import ru.dankoy.telegrambot.config.TelegramBotProperties;

@ExtendWith(MockitoExtension.class)
class LocalisationServiceImplTest {

  @InjectMocks private LocalisationServiceImpl localisationService;

  @Mock private MessageSource messageSource;

  @Spy
  private final LocaleConfig localeConfig =
      new TelegramBotProperties(
          "name", "name", new Locale[] {Locale.of("en"), Locale.of("ru")}, Locale.of("en"));

  private final Locale locale = Locale.of("en");

  @DisplayName("getLocalizedMessage expects correct invocation")
  @Test
  void getLocalizedMessage() {

    localisationService.getLocalizedMessage("code", new Object[] {}, locale);

    verify(messageSource, times(1)).getMessage("code", new Object[] {}, locale);
  }

  @DisplayName("getLocalizedMessage with null code expects NullPointerException")
  @Test
  void getLocalizedMessageTest_ThrowsNullPointerException() {

    assertThatThrownBy(() -> localisationService.getLocalizedMessage(null, new Object[] {}, locale))
        .isInstanceOf(NullPointerException.class);

    verify(messageSource, times(0)).getMessage("code", new Object[] {}, locale);
  }

  @DisplayName("getLocalizedMessage with null locale expects default locale usage")
  @Test
  void getLocalizedMessageTest_expectsDefaultLocale() {

    localisationService.getLocalizedMessage("code", new Object[] {}, null);

    verify(messageSource, times(1)).getMessage("code", new Object[] {}, locale);
  }
}
