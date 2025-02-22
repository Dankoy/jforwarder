package ru.dankoy.telegramchatservice;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableEncryptableProperties
@SpringBootApplication
public class SubscriptionsHolderApplication {

  public static void main(String[] args) {
    SpringApplication.run(SubscriptionsHolderApplication.class, args);
  }
}
