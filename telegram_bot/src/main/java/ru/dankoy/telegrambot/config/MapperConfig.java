package ru.dankoy.telegrambot.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

  // @Bean
  // public JsonMapperBuilderCustomizer jacksonCustomizer() {

  //   //TODO: look if it even needed in jackson3. Maybe just remove it
  // Although Jackson 3 uses tools.jackson
  //   return builder -> builder
  //   .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
  //   .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
  //   .build();
  // }
}
