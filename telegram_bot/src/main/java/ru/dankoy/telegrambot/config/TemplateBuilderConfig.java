package ru.dankoy.telegrambot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import ru.dankoy.telegrambot.core.service.template.TemplateBuilder;
import ru.dankoy.telegrambot.core.service.template.TemplateBuilderImpl;

@Configuration
public class TemplateBuilderConfig {

  @Bean
  public TemplateBuilder templateBuilder(FreeMarkerConfigurer freeMarkerConfigurer) {

    freeMarkerConfigurer.getConfiguration().setLocalizedLookup(true);

    return new TemplateBuilderImpl(freeMarkerConfigurer);
  }
}
