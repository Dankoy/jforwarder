package ru.dankoy.telegrambot.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.dankoy.telegrambot.core.service.template.TemplateBuilder;
import ru.dankoy.telegrambot.core.service.template.TemplateBuilderImpl;

@Configuration
public class TemplateBuilderConfig {


  @Bean
  TemplateBuilder templateBuilder(@Value("${application.templates.dir}") String templatesDir) {
    return new TemplateBuilderImpl(templatesDir);
  }


}
