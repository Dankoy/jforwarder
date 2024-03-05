package ru.dankoy.subscriptionsholder.subscriptions_holder;

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
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller.ChannelController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller.ChannelSubController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller.ChatController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller.CommunityController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller.CommunitySubController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller.OrderController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller.SentCoubsRegistryController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller.SubscriptionController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller.TagController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller.TagSubController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.ChannelRepository;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.ChannelSubRepository;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.CommunityRepository;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.CommunitySubRepository;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.OrderRepository;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.ScopeRepository;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.SectionRepository;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.SentCoubsRegistryRepository;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.SubscriptionRepository;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.TagRepository;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.TagSubRepository;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.TelegramChatRepository;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.TypeRepository;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.ChannelService;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.ChannelSubService;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.CommunityService;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.CommunitySubService;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.OrderService;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.ScopeService;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.SectionService;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.SentCoubsRegistryService;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.SubscriptionService;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.TagService;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.TagSubService;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.TelegramChatService;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.TypeService;

@DisplayName("Test default context ")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(
    exclude = {EurekaClientAutoConfiguration.class, EurekaDiscoveryClientConfiguration.class})
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

  @Autowired ApplicationContext context;

  @DisplayName("all default necessary repository beans should be created")
  @Test
  void contextLoads() {

    // repos
    var channelRepository = context.getBean(ChannelRepository.class);
    var channelSubRepository = context.getBean(ChannelSubRepository.class);
    var communityRepository = context.getBean(CommunityRepository.class);
    var communitySubRepository = context.getBean(CommunitySubRepository.class);
    var orderRepository = context.getBean(OrderRepository.class);
    var scopeRepository = context.getBean(ScopeRepository.class);
    var sectionRepository = context.getBean(SectionRepository.class);
    var sentCoubsRegistryRepository = context.getBean(SentCoubsRegistryRepository.class);
    var subscriptionRepository = context.getBean(SubscriptionRepository.class);
    var tagRepository = context.getBean(TagRepository.class);
    var tagSubRepository = context.getBean(TagSubRepository.class);
    var telegramChatRepository = context.getBean(TelegramChatRepository.class);
    var typeRepository = context.getBean(TypeRepository.class);

    assertNotNull(channelRepository);
    assertNotNull(channelSubRepository);
    assertNotNull(communityRepository);
    assertNotNull(communitySubRepository);
    assertNotNull(orderRepository);
    assertNotNull(scopeRepository);
    assertNotNull(sectionRepository);
    assertNotNull(sentCoubsRegistryRepository);
    assertNotNull(subscriptionRepository);
    assertNotNull(tagRepository);
    assertNotNull(tagSubRepository);
    assertNotNull(telegramChatRepository);
    assertNotNull(typeRepository);
  }

  @DisplayName("all default necessary services beans should be created")
  @Test
  void contextServicesLoads() {

    // services
    var channelService = context.getBean(ChannelService.class);
    var channelSubService = context.getBean(ChannelSubService.class);
    var communityService = context.getBean(CommunityService.class);
    var communitySubService = context.getBean(CommunitySubService.class);
    var orderService = context.getBean(OrderService.class);
    var scopeService = context.getBean(ScopeService.class);
    var sectionService = context.getBean(SectionService.class);
    var sentCoubsRegistryService = context.getBean(SentCoubsRegistryService.class);
    var subscriptionService = context.getBean(SubscriptionService.class);
    var tagService = context.getBean(TagService.class);
    var tagSubService = context.getBean(TagSubService.class);
    var telegramChatService = context.getBean(TelegramChatService.class);
    var typeService = context.getBean(TypeService.class);

    assertNotNull(channelService);
    assertNotNull(channelSubService);
    assertNotNull(communityService);
    assertNotNull(communitySubService);
    assertNotNull(orderService);
    assertNotNull(scopeService);
    assertNotNull(sectionService);
    assertNotNull(sentCoubsRegistryService);
    assertNotNull(subscriptionService);
    assertNotNull(tagService);
    assertNotNull(tagSubService);
    assertNotNull(telegramChatService);
    assertNotNull(typeService);
  }

  @DisplayName("all default necessary controller beans should be created")
  @Test
  void contextControllersLoads() {

    // controller
    var channelController = context.getBean(ChannelController.class);
    var channelSubController = context.getBean(ChannelSubController.class);
    var chatController = context.getBean(ChatController.class);
    var communityController = context.getBean(CommunityController.class);
    var communitySubController = context.getBean(CommunitySubController.class);
    var orderController = context.getBean(OrderController.class);
    var sentCoubsRegistryController = context.getBean(SentCoubsRegistryController.class);
    var subscriptionController = context.getBean(SubscriptionController.class);
    var tagController = context.getBean(TagController.class);
    var tagSubController = context.getBean(TagSubController.class);

    assertNotNull(channelController);
    assertNotNull(channelSubController);
    assertNotNull(chatController);
    assertNotNull(communityController);
    assertNotNull(communitySubController);
    assertNotNull(orderController);
    assertNotNull(sentCoubsRegistryController);
    assertNotNull(subscriptionController);
    assertNotNull(tagController);
    assertNotNull(tagSubController);
  }
}
