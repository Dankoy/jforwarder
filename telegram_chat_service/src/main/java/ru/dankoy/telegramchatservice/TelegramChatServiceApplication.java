package ru.dankoy.telegramchatservice;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableEncryptableProperties
@SpringBootApplication
public class TelegramChatServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(TelegramChatServiceApplication.class, args);
  }
}
