package ru.dankoy.coubtagssearcher;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration;
import org.springframework.context.ApplicationContext;
import ru.dankoy.coubtagssearcher.core.controller.ChannelController;
import ru.dankoy.coubtagssearcher.core.controller.TagController;
import ru.dankoy.coubtagssearcher.core.service.channel.ChannelServiceImpl;
import ru.dankoy.coubtagssearcher.core.service.tag.TagServiceImpl;

@DisplayName("Test default context ")
@SpringBootTest
@EnableAutoConfiguration(
    exclude = {EurekaClientAutoConfiguration.class, EurekaDiscoveryClientConfiguration.class})
class CoubTagsSearcherAppTests {

  @Autowired ApplicationContext context;

  @Test
  void contextLoads() {

    var channelService = context.getBean(ChannelServiceImpl.class);
    var tagService = context.getBean(TagServiceImpl.class);
    var channelController = context.getBean(ChannelController.class);
    var tagController = context.getBean(TagController.class);

    assertNotNull(channelService);
    assertNotNull(tagService);
    assertNotNull(channelController);
    assertNotNull(tagController);
  }
}
