package ru.dankoy.subscriptions_scheduler;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import ru.dankoy.subscriptions_scheduler.config.properties.SchedulerPropertiesImpl;
import ru.dankoy.subscriptions_scheduler.core.controller.ChatController;
import ru.dankoy.subscriptions_scheduler.core.feign.SubscriptionsHolderFeign;
import ru.dankoy.subscriptions_scheduler.core.feign.TelegramChatServiceFeign;
import ru.dankoy.subscriptions_scheduler.core.service.chat.ChatServiceImpl;
import ru.dankoy.subscriptions_scheduler.core.service.chat.TelegramChatServiceImpl;
import ru.dankoy.subscriptions_scheduler.core.service.scheduler.ChatCheckerServiceImpl;
import ru.dankoy.subscriptions_scheduler.core.service.scheduler.TelegramChatServiceCheckerImpl;
import ru.dankoy.subscriptions_scheduler.core.service.subscriptions.SubscriptionsHolderServiceImpl;

@SpringBootTest
class SubscriptionsSchedulerApplicationTests {

  @Autowired ApplicationContext context;

  @Test
  void contextLoads() {

    var schedulerPropertiesImpl = context.getBean(SchedulerPropertiesImpl.class);
    var chatController = context.getBean(ChatController.class);
    var subscriptionsHolderFeign = context.getBean(SubscriptionsHolderFeign.class);
    var telegramChatServiceFeign = context.getBean(TelegramChatServiceFeign.class);

    var chatServiceImpl = context.getBean(ChatServiceImpl.class);
    var telegramChatServiceImpl = context.getBean(TelegramChatServiceImpl.class);
    var chatCheckerServiceImpl = context.getBean(ChatCheckerServiceImpl.class);
    var telegramChatServiceCheckerImpl = context.getBean(TelegramChatServiceCheckerImpl.class);
    var subscriptionsHolderServiceImpl = context.getBean(SubscriptionsHolderServiceImpl.class);

    assertNotNull(schedulerPropertiesImpl);
    assertNotNull(chatController);
    assertNotNull(subscriptionsHolderFeign);
    assertNotNull(telegramChatServiceFeign);

    assertNotNull(chatServiceImpl);
    assertNotNull(telegramChatServiceImpl);
    assertNotNull(chatCheckerServiceImpl);
    assertNotNull(telegramChatServiceCheckerImpl);
    assertNotNull(subscriptionsHolderServiceImpl);
  }
}
