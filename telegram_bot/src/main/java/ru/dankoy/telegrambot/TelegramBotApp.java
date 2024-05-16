package ru.dankoy.telegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import ru.dankoy.telegrambot.config.bot.TelegramBotConfig;

@EnableCaching
@EnableFeignClients
@SpringBootApplication
@ComponentScan(
    excludeFilters = {
      @ComponentScan.Filter(
          type = FilterType.ASSIGNABLE_TYPE,
          value = {TelegramBotConfig.class})
    })
public class TelegramBotApp {

  public static void main(String[] args) {
    SpringApplication.run(TelegramBotApp.class, args);
  }
}
