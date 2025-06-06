package ru.dankoy.telegrambot;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import ru.dankoy.telegrambot.config.bot.configuration.botflow.BotConfigurationImpl;
import ru.dankoy.telegrambot.core.service.bot.TelegramClientService;
import ru.dankoy.telegrambot.core.service.bot.commands.CommandsHolder;
import ru.dankoy.telegrambot.core.service.channel.ChannelServiceImpl;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatServiceImpl;
import ru.dankoy.telegrambot.core.service.community.CommunityServiceImpl;
import ru.dankoy.telegrambot.core.service.coubtags.CoubSmartSearcherServiceImpl;
import ru.dankoy.telegrambot.core.service.localeprovider.LocaleProviderImpl;
import ru.dankoy.telegrambot.core.service.localization.LocalisationServiceImpl;
import ru.dankoy.telegrambot.core.service.order.OrderServiceImpl;
import ru.dankoy.telegrambot.core.service.subscription.ChannelSubscriptionServiceImpl;
import ru.dankoy.telegrambot.core.service.subscription.CommunitySubscriptionServiceImpl;
import ru.dankoy.telegrambot.core.service.subscription.TagSubscriptionServiceImpl;
import ru.dankoy.telegrambot.core.service.tag.TagServiceImpl;
import ru.dankoy.telegrambot.core.service.template.TemplateBuilderImpl;

@DisplayName("Test default context ")
@SpringBootTest
@EnableAutoConfiguration(
    exclude = {EurekaClientAutoConfiguration.class, EurekaDiscoveryClientConfiguration.class})
class TelegramBotAppTests {

  @Autowired private ApplicationContext context;

  // Mock telegrambots beans that actually connects to telegram api external service
  @MockBean private TelegramBotsApi telegramBotsApi;

  @MockBean private TelegramClientService telegramBot;

  @Test
  void contextLoads() {

    var commandsHolder = context.getBean(CommandsHolder.class);
    var channelService = context.getBean(ChannelServiceImpl.class);
    var telegramChatService = context.getBean(TelegramChatServiceImpl.class);
    var communityService = context.getBean(CommunityServiceImpl.class);
    var coubSmartSearcherService = context.getBean(CoubSmartSearcherServiceImpl.class);
    var localeProvider = context.getBean(LocaleProviderImpl.class);
    var localisationService = context.getBean(LocalisationServiceImpl.class);
    var orderService = context.getBean(OrderServiceImpl.class);
    var channelSubscriptionService = context.getBean(ChannelSubscriptionServiceImpl.class);
    var communitySubscriptionService = context.getBean(CommunitySubscriptionServiceImpl.class);
    var tagSubscriptionService = context.getBean(TagSubscriptionServiceImpl.class);
    var tagService = context.getBean(TagServiceImpl.class);
    var templateBuilder = context.getBean(TemplateBuilderImpl.class);
    var botConfiguration = context.getBean(BotConfigurationImpl.class);

    assertNotNull(commandsHolder);
    assertNotNull(channelService);
    assertNotNull(telegramChatService);
    assertNotNull(communityService);
    assertNotNull(coubSmartSearcherService);
    assertNotNull(localeProvider);
    assertNotNull(localisationService);
    assertNotNull(orderService);
    assertNotNull(channelSubscriptionService);
    assertNotNull(communitySubscriptionService);
    assertNotNull(tagSubscriptionService);
    assertNotNull(tagService);
    assertNotNull(templateBuilder);
    assertNotNull(botConfiguration);
  }
}
