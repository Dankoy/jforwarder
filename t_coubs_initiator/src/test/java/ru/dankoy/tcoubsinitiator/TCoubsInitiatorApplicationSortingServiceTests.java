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
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import ru.dankoy.tcoubsinitiator.core.service.coubfinder.CoubFinderServiceImpl;

@DisplayName("Test default context ")
@SpringBootTest
@EnableAutoConfiguration(
    exclude = {EurekaClientAutoConfiguration.class, EurekaDiscoveryClientConfiguration.class})
@TestPropertySource(properties = "coub.sorting=by-published-date")
@Import(CoubFinderServiceImpl.class)
class TCoubsInitiatorApplicationSortingServiceTests {

  @Autowired ApplicationContext context;

  @Test
  void contextLoads() {

    var coubFinderServiceWithSorting = context.getBean(CoubFinderServiceImpl.class);

    assertNotNull(coubFinderServiceWithSorting);
  }
}
