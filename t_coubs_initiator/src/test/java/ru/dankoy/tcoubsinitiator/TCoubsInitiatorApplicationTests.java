package ru.dankoy.tcoubsinitiator;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration;
import org.springframework.context.ApplicationContext;
import ru.dankoy.tcoubsinitiator.core.service.channelsubscription.ChannelSubscriptionServiceImpl;
import ru.dankoy.tcoubsinitiator.core.service.coub.CoubServiceImpl;
import ru.dankoy.tcoubsinitiator.core.service.coubfinder.CoubFinderServiceWithoutSorting;
import ru.dankoy.tcoubsinitiator.core.service.filter.FilterByRegistryServiceImpl;
import ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice.MessageProducerChannelSubscriptionServiceImpl;
import ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice.MessageProducerCommunitySubscriptionServiceImpl;
import ru.dankoy.tcoubsinitiator.core.service.messageproducerconnectorservice.MessageProducerTagSubscriptionServiceImpl;
import ru.dankoy.tcoubsinitiator.core.service.permalinkcreator.PermalinkCreatorServiceImpl;
import ru.dankoy.tcoubsinitiator.core.service.registry.SentCoubsRegistryServiceImpl;
import ru.dankoy.tcoubsinitiator.core.service.sheduler.SchedulerSubscriptionServiceChannel;
import ru.dankoy.tcoubsinitiator.core.service.sheduler.SchedulerSubscriptionServiceCommunitySection;
import ru.dankoy.tcoubsinitiator.core.service.sheduler.SchedulerSubscriptionServiceTag;
import ru.dankoy.tcoubsinitiator.core.service.subscription.SubscriptionServiceImpl;
import ru.dankoy.tcoubsinitiator.core.service.tagsubscription.TagSubscriptionServiceImpl;

@DisplayName("Test default context ")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(
    exclude = {EurekaClientAutoConfiguration.class, EurekaDiscoveryClientConfiguration.class})
class TCoubsInitiatorApplicationTests {

  @Autowired ApplicationContext context;

  @Test
  void contextLoads() {

    // repos
    var channelSubscriptionService = context.getBean(ChannelSubscriptionServiceImpl.class);
    var coubService = context.getBean(CoubServiceImpl.class);
    var coubFinderServiceWithoutSorting = context.getBean(CoubFinderServiceWithoutSorting.class);
    var filterByRegistryService = context.getBean(FilterByRegistryServiceImpl.class);
    var messageProducerChannelSubscriptionService =
        context.getBean(MessageProducerChannelSubscriptionServiceImpl.class);
    var messageProducerCommunitySubscriptionService =
        context.getBean(MessageProducerCommunitySubscriptionServiceImpl.class);
    var messageProducerTagSubscriptionService =
        context.getBean(MessageProducerTagSubscriptionServiceImpl.class);
    var permalinkCreatorService = context.getBean(PermalinkCreatorServiceImpl.class);
    var sentCoubsRegistryRepository = context.getBean(SentCoubsRegistryServiceImpl.class);
    var schedulerSubscriptionServiceChannel =
        context.getBean(SchedulerSubscriptionServiceChannel.class);
    var schedulerSubscriptionServiceTag = context.getBean(SchedulerSubscriptionServiceTag.class);
    var schedulerSubscriptionServiceCommunitySection =
        context.getBean(SchedulerSubscriptionServiceCommunitySection.class);
    var subscriptionService = context.getBean(SubscriptionServiceImpl.class);
    var tagSubscriptionService = context.getBean(TagSubscriptionServiceImpl.class);

    assertNotNull(channelSubscriptionService);
    assertNotNull(coubService);
    assertNotNull(coubFinderServiceWithoutSorting);
    assertNotNull(filterByRegistryService);
    assertNotNull(messageProducerChannelSubscriptionService);
    assertNotNull(messageProducerCommunitySubscriptionService);
    assertNotNull(messageProducerTagSubscriptionService);
    assertNotNull(sentCoubsRegistryRepository);
    assertNotNull(permalinkCreatorService);
    assertNotNull(schedulerSubscriptionServiceChannel);
    assertNotNull(schedulerSubscriptionServiceTag);
    assertNotNull(schedulerSubscriptionServiceCommunitySection);
    assertNotNull(subscriptionService);
    assertNotNull(tagSubscriptionService);
  }
}
