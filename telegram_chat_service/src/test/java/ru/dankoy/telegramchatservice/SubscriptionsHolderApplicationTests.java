package ru.dankoy.telegramchatservice;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import ru.dankoy.telegramchatservice.core.controller.ChatController;
import ru.dankoy.telegramchatservice.core.repository.TelegramChatRepository;

import ru.dankoy.telegramchatservice.core.service.TelegramChatService;

@DisplayName("Test default context ")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = { EurekaClientAutoConfiguration.class, EurekaDiscoveryClientConfiguration.class })
class SubscriptionsHolderApplicationTests {

  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

  @BeforeAll
  static void beforeAll() {
    postgres.start();
  }

  @AfterAll
  static void afterAll() {
    postgres.stop();
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @Autowired
  ApplicationContext context;

  @DisplayName("all default necessary repository beans should be created")
  @Test
  void contextLoads() {

    // repos

    var telegramChatRepository = context.getBean(TelegramChatRepository.class);

    assertNotNull(telegramChatRepository);

  }

  @DisplayName("all default necessary services beans should be created")
  @Test
  void contextServicesLoads() {

    // services

    var telegramChatService = context.getBean(TelegramChatService.class);

    assertNotNull(telegramChatService);
  }

  @DisplayName("all default necessary controller beans should be created")
  @Test
  void contextControllersLoads() {

    // controller

    var chatController = context.getBean(ChatController.class);

    assertNotNull(chatController);

  }
}
